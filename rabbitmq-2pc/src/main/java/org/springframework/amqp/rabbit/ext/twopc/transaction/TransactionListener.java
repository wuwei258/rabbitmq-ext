package org.springframework.amqp.rabbit.ext.twopc.transaction;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.LocalTransactionState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;


/**
 * @ClassName TransactionListener
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
public interface TransactionListener {
    /**
     * When send transactional prepare(half) message succeed, this method will be invoked to execute local transaction.
     *
     * @param msg  Half(prepare) message
     * @param args Custom business parameter
     * @return Transaction state
     */
    LocalTransactionState executeLocalTransaction(final TransactionId msg, final Object... args);

    /**
     * When no response to prepare(half) message. broker will send check message to check the transaction status, and
     * this method will be invoked to get local transaction status.
     *
     * @param msg Check message
     * @return Transaction state
     */
    LocalTransactionState checkLocalTransaction(final TransactionId msg);
}