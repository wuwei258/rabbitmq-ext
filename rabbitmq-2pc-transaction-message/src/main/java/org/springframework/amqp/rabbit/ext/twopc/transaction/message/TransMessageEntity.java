package org.springframework.amqp.rabbit.ext.twopc.transaction.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
 * @className TransMessageEntity
 * @author wuwei
 * @description
 * @date 2019/12/20 14:28
 **/
@Data
@Builder
public class TransMessageEntity implements Serializable {
    private static final long serialVersionUID = -419825158877639355L;
    private Long id;
    private String transactionId;
    private String queueName;
    private String clazz;
    private Integer sendTimes;
    private String messageContent;
    private MessageState messageState;
    private LocalTransactionState localTransactionState;

}
