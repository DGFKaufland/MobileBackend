package de.kaufland.ksilence.service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.exception.EmptyParameterException;
import de.kaufland.ksilence.exception.EntityNotFoundException;
import de.kaufland.ksilence.model.MobileContact;
import de.kaufland.ksilence.model.MobileNotification;
import de.kaufland.ksilence.model.OperatingSystem;
import de.kaufland.ksilence.model.Status;
import de.kaufland.ksilence.repository.ContactRepository;
import de.kaufland.ksilence.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class SnoozeService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ContactRepository contactRepository;

    public Iterable<MobileNotification> readAll() {
        log.debug("Read all NOTIFICATIONS");
        return notificationRepository.findAll();
    }
    
    
   
    @Scheduled(cron = "0 1 1 ? * *")
    @Scheduled(fixedRate = 30000) //Millisekunden
    public void resetCache() throws IOException {
    
   	 //GoogleGuava Methode evtl verwenden statt List
   	 //http://stackoverflow.com/questions/23935662/print-records-from-findall-in-crudrepository
       List <MobileNotification> test = notificationRepository.findAllList();
       
       for(int i=0; i<test.size(); i++)
       {
          
    	   // Die Ausgabe findet fünfmal statt (von 0 bis 4)
           System.out.println("i ist "+i);
           Long st = test.get(i).getSnooze_time();
          
           if (st == 0){
           }
           else if (st > 1){
        	   //hier bitte die snooze time updaten (-1 Minute) setzen
        	   Long st_neu = st -1;
        	   test.get(i).setSnooze_time(st_neu);
        	   notificationRepository.save(test.get(i));
           }          
           else if(st <= 1){
        	   Long st_neu = st -1;
        	   test.get(i).setSnooze_time(st_neu);
        	   test.get(i).setState(Status.OPEN);
        	   notificationRepository.save(test.get(i));
        	   
        	   update(test.get(i));     	   
           }
           
           
       }
       
       
    }
    

    private void sendNotification(MobileNotification pNotification, String message) throws IOException{
        URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "key=" + Api.API_KEY);

        OutputStream out = conn.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(message);
        writer.close();
        out.close();

        int responseCode = conn.getResponseCode();
        if(responseCode == 200){
            log.debug("NOTIFICATION send to GCM Connection Server");
        } else {
            log.debug("NOTIFICATION not send to GCM. Response code: " + responseCode);
        }
        conn.disconnect();
    }

    public void update(MobileNotification pNotification) throws IOException {

        // Send updated status to sender contact
        MobileContact fromContact = contactRepository.findById(pNotification.getFromContactId());
        MobileContact toContact = contactRepository.findById(pNotification.getToContactId());

        if(fromContact.getOs() == OperatingSystem.ANDROID) {
            String msg = "";
            msg += "{";
            msg += "\"to\" : \"" + fromContact.getRegistrationToken() + "\", ";
            msg += "\"data\" {";
            msg += "\"title\" : \"" + toContact.getName() + ": " + pNotification.getState() + "\", ";
            msg += "\"message\" : \"" + pNotification.getBody() + "\", ";
            msg += "\"notId\" : \"" + pNotification.getId() + "\", ";
            msg += "}";
            msg += "}";

            sendNotification(pNotification, msg);

        } else {
            ApnsService service =
                    APNS.newService()
                            .withCert("/Users/tdit0703/IdeaProjects/AppServer/src/main/resources/KSilenceDevZertifikate.p12", "Kaufland16")
                            .withSandboxDestination()
                            .build();

            String payload = APNS.newPayload().alertBody(pNotification.getBody()).alertTitle(toContact.getName() + ": " + pNotification.getState()).build();
            String token = fromContact.getRegistrationToken();
            service.push(token, payload);
        }
    }

    
}
