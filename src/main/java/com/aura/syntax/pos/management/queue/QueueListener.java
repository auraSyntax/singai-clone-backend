package com.aura.syntax.pos.management.queue;

public interface QueueListener {
    void execute(QueueMessage queueMessage);
}
