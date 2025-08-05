package com.aura.syntax.pos.management.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueueMessage<T> {
    private T data;
}
