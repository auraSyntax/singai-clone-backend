package com.aura.syntax.pos.management.config;


import com.aura.syntax.pos.management.queue.Queue;
import com.aura.syntax.pos.management.service.listener.EmailSenderQueueListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public Queue transceiver(EmailSenderQueueListener listener) {
        Queue queue = Queue.builder()
                .listener(listener)
                .consumerCount(5)
                .build();
        return queue;
    }
}
