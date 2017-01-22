package de.kaufland.ksilence.service;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import de.kaufland.ksilence.api.Api;
import de.kaufland.ksilence.exception.EmptyParameterException;
import de.kaufland.ksilence.exception.EntityNotFoundException;
import de.kaufland.ksilence.model.MobileContact;
import de.kaufland.ksilence.model.MobileNotification;
import de.kaufland.ksilence.model.OperatingSystem;
import de.kaufland.ksilence.repository.ContactRepository;
import de.kaufland.ksilence.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ContactRepository contactRepository;

    public Iterable<MobileNotification> readAll() {
        log.debug("Read all NOTIFICATIONS");
        return notificationRepository.findAll();
    }

    public MobileNotification create(MobileNotification pNotification) throws IOException {
        log.debug("Create NOTIFICATION: " + pNotification);

        if(pNotification.getToContactId() == 0 || pNotification.getToContactId() < 0){
            throw new EmptyParameterException("ToContactId");
        } else if(pNotification.getFromContactId() == 0 || pNotification.getFromContactId() < 0){
            throw new EmptyParameterException("FromContactId");
        } else if(pNotification.getBody() == null || pNotification.getBody() == "" || pNotification.getBody().isEmpty()){
            throw new EmptyParameterException("Body");
        } else if (contactRepository.findById(pNotification.getToContactId()) == null){
            throw new EntityNotFoundException();
        
        } 
        
        
        /*
        
        else if (pNotification.getSnooze_time() == 0 || pNotification.getToContactId() < 0){
        	 throw new EmptyParameterException("Snooze_time"); 
            
        } else if(pNotification.getMessage() == null || pNotification.getMessage() == "" || pNotification.getMessage().isEmpty()){
            throw new EmptyParameterException("Message");
        }
         */
            
            
            
            
        else if (contactRepository.findById(pNotification.getFromContactId()) == null){
            throw new EntityNotFoundException();
        }

        // Send notification AFTER saving into DB
        // Important, because id is needed for function of notification action buttons
        MobileNotification newNotification = notificationRepository.save(pNotification);

        // Build message
        MobileContact fromContact = contactRepository.findById(pNotification.getFromContactId());
        MobileContact toContact = contactRepository.findById(pNotification.getToContactId());
        String msg = "";

        
        //hier muss evtl. noch der Snooze Modus beruecksichtig werden!!
        if(toContact.getOs() == OperatingSystem.ANDROID) {
            msg += "{";
            msg += "\"to\" : \"" + toContact.getRegistrationToken() + "\", ";
            msg += "\"data\" {";
            msg += "\"title\" : \"" + fromContact.getName() + "\", ";
            msg += "\"message\" : \"" + pNotification.getBody() + "\", ";
            msg += "\"notId\" : \"" + pNotification.getId() + "\", ";

            msg += "\"actions\" : [";
            msg += "{";
            msg += "\"icon\" : \"inprocess\", ";
            msg += "\"title\" : \"IN PROCESS\", ";
            msg += "\"callback\" : \"app.inprocess\"";
            msg += "}";
            msg += "{";
            msg += "\"icon\" : \"done\", ";
            msg += "\"title\" : \"DONE\", ";
            msg += "\"callback\" : \"app.done\"";
            msg += "}";
            msg += "]";

            msg += "}";
            msg += "}";

            sendNotification(newNotification, msg);

        } else {
            ApnsService service =
                    APNS.newService()
                            .withCert("/Users/tdit0703/IdeaProjects/AppServer/src/main/resources/KSilenceDevZertifikate.p12", "Kaufland16")
                            .withSandboxDestination()
                            .build();

            String payload = APNS.newPayload().alertBody(newNotification.getBody()).alertTitle(newNotification.getFromContactName()).actionKey("actions").build();
            String token = toContact.getRegistrationToken();
            service.push(token, payload);
        }

        return newNotification;
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
        log.debug("Update NOTIFICATION: (" + pNotification.getId() + ")");
        MobileNotification existingNotification = notificationRepository.findById(pNotification.getId());

        if (existingNotification == null) {
            throw new EntityNotFoundException();
        }

        log.debug("From: " + existingNotification);
        MobileNotification updatedNotification = notificationRepository.save(pNotification);
        log.debug("To:   " + updatedNotification);

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

            sendNotification(updatedNotification, msg);

        } else {
            ApnsService service =
                    APNS.newService()
                            .withCert("/Users/tdit0703/IdeaProjects/AppServer/src/main/resources/KSilenceDevZertifikate.p12", "Kaufland16")
                            .withSandboxDestination()
                            .build();

            String payload = APNS.newPayload().alertBody(updatedNotification.getBody()).alertTitle(toContact.getName() + ": " + pNotification.getState()).build();
            String token = fromContact.getRegistrationToken();
            service.push(token, payload);
        }
    }

    public MobileNotification read(String pNotificationId) {
        log.debug("Read NOTIFICATION (" + pNotificationId + ")");
        MobileNotification n = notificationRepository.findById(Long.valueOf(pNotificationId));

        if(n == null){
            throw new EntityNotFoundException();
        }

        return n;
    }
}
