package org.springframework.amqp.rabbit.ext.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;

import java.io.Serializable;

@Data
@Builder
public class SendMessage implements TransactionId, Serializable {
    private static final long serialVersionUID = 4298528335976490983L;
    private Long id;

    private String content;

    @Override
    public String transactionId() {
        return id.toString();
    }
}
