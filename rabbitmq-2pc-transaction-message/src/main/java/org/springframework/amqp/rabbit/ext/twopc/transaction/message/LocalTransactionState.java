
package org.springframework.amqp.rabbit.ext.twopc.transaction.message;


/**
 * @className LocalTransactionState
 * @author wuwei
 * @description
 * @date 2019/12/20 14:28
 **/
public enum LocalTransactionState {
    COMMIT_MESSAGE,
    ROLLBACK_MESSAGE,
    UNKNOW,
}
