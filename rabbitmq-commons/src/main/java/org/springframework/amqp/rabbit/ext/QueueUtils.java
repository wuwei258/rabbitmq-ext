package org.springframework.amqp.rabbit.ext;

import org.springframework.amqp.core.Queue;

/**
 * @ClassName QueueUtils
 * @Author wuwei
 * @Description 定义队列工具
 * @Date 2019/12/20 13:53
 **/
public class QueueUtils {
    /**
     * 定义默认队列
     *
     * @param name 队列名称
     * @return
     */
    public static Queue createDefaultQueue(String name) {
        return new Queue(name, true, false, false);
    }

    /**
     * 定义非持久化队列
     *
     * @param name
     * @return
     */
    public static Queue createNonPersistentQueue(String name) {
        return new Queue(name, false, false, false);
    }
}
