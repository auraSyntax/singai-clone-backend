package com.aura.syntax.pos.management.service.listener;

import com.aura.syntax.pos.management.queue.QueueListener;
import com.aura.syntax.pos.management.queue.QueueMessage;
import com.aura.syntax.pos.management.service.EmailData;
import com.aura.syntax.pos.management.service.support.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSenderQueueListener implements QueueListener {
    private final EmailSender emailSender;

    @Override
    public void execute(QueueMessage queueMessage) {
        if (queueMessage != null) {
            try {
                log.info("Start sending mail to : " + ((EmailData) queueMessage.getData()).getRecipients());
                EmailData data = (EmailData) queueMessage.getData();
                emailSender.sendHtmlMessage(data);
                log.info("Mail sent to : " + data.getRecipients());
            } catch (Exception e) {
                log.error("Error occurred while sending mail: " + queueMessage.getData(), e);
            }
        }
    }
}
