package de.kaufland.ksilence.service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.model.MobileContact;
import de.kaufland.ksilence.model.MobileNotification;
import de.kaufland.ksilence.model.OperatingSystem;
import de.kaufland.ksilence.model.Status;
import de.kaufland.ksilence.repository.ContactRepository;
import de.kaufland.ksilence.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@EnableScheduling
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
    
    
   
    @Scheduled(fixedRate = 60000) //Millisekunden
    public void resetCache() throws IOException {
    
   	 //GoogleGuava Methode evtl verwenden
       List <MobileNotification> allNotifications = (List <MobileNotification>) notificationRepository.findAll();
         
       for(int i=0; i<allNotifications.size(); i++)
       {
          
           //System.out.println("i ist "+i);
           Long snooze_time = allNotifications.get(i).getSnooze_time();
           Status state = allNotifications.get(i).getState();
           //System.out.println("state " + i + " ist: " + state);
          
           if (snooze_time == 0){
           }
           //&& state == state.SNOOZE
           else if (snooze_time > 1  && state == state.SNOOZE){
        	   //hier bitte die snooze time updaten (-1 Minute) setzen
        	   Long snooze_time_new = snooze_time -1;
        	   allNotifications.get(i).setSnooze_time(snooze_time_new);
        	   notificationRepository.save(allNotifications.get(i));
        	   
        	   //System.out.println("SnoozeTimeUpdateID: " + allNotifications.get(i).getId() + " Body " + allNotifications.get(i).getBody());
           }          
           else if(snooze_time <= 1 && state == state.SNOOZE){
        	   Long snooze_time_new = snooze_time -1;
        	   allNotifications.get(i).setSnooze_time(snooze_time_new);
        	   allNotifications.get(i).setState(Status.OPEN);
        	   notificationRepository.save(allNotifications.get(i));
        	   
        	   update(allNotifications.get(i)); 
        	   
        	   //System.out.println("SnoozeTimeUpdateAndNotificationToSmartwatch: " + allNotifications.get(i).getId() + " Body " + allNotifications.get(i).getBody());
        	   
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
