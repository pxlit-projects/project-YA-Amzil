package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Fe using a Mail API to send the notification would be applicable here.
     *
     * @param notification
     */

    public void sendMessage(Notification notification) {
        log.info("Received notification...");
        log.info("Sending... {}", notification.getMessage());
        log.info("TO {}", notification.getSender());
    }
}
