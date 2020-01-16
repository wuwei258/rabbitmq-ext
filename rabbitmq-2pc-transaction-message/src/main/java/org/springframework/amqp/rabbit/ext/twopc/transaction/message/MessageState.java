package org.springframework.amqp.rabbit.ext.twopc.transaction.message;


/**
 * @className MessageState
 * @author wuwei
 * @description
 * @date 2019/12/20 14:28
 **/
public enum MessageState {
    PREPARE(0), COMMIT(1), RE_SEND(2);

    private int code;

    public int getCode() {
        return code;
    }

    MessageState(int code) {
        this.code = code;
    }
}
