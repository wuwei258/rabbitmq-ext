package org.springframework.amqp.rabbit.ext.twopc.transaction.message;


/**
 * @className MessageState
 * @author wuwei
 * @description
 * @date 2019/12/20 14:28
 **/
public enum MessageState {
    /**
     * 消息prepare状态
     * 等待本地事务执行
     */
    PREPARE(0),
    /**
     * 消息发送至mq中等待ack确认
     */
    SEND(1),
    /**
     * mq confirm成功，消息已成功投递至交换机中
     */
    COMMIT(2),
    /**
     * confirm失败消息未成功放入交换机
     * 等待下次丢入
     */
    RE_SEND(3);

    private int code;

    public int getCode() {
        return code;
    }

    MessageState(int code) {
        this.code = code;
    }
}
