package org.springframework.amqp.rabbit.ext.conf;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName SpringRetryTransactionMessageHolder
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 11:52
 **/
@Slf4j
public final class SpringRetryMessageCorrelationHolder {
    private static ConcurrentHashMap<String, Boolean> RETRY_HOLDER = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Integer> RETRY_COUNT_HOLDER = new ConcurrentHashMap<>();

    /**
     * 增加重试计数器信息
     *
     * @param id
     */
    public static void addRetryTimes(String id) {
        Integer retryTimes = getRetryTimes(id);
        if (null == retryTimes) {
            retryTimes = 0;
        } else {
            retryTimes++;
        }
        RETRY_COUNT_HOLDER.put(id, retryTimes);
    }

    /**
     * 获取重试的此时
     *
     * @param id
     * @return
     */
    public static Integer getRetryTimes(String id) {
        return RETRY_COUNT_HOLDER.get(id);
    }

    /**
     * 设置变量值
     *
     * @param id
     */
    public static void set(String id) {
        RETRY_HOLDER.put(id, true);
    }

    /**
     * 获取当中是否是重试
     *
     * @return
     */
    public static Boolean get(String id) {
        Boolean retry = RETRY_HOLDER.get(id);
        log.info("当前线程:{}", retry);
        return null == retry ? false : !retry;
    }

    /**
     * 清空当前信息
     */
    public static void clean(String id) {
        RETRY_COUNT_HOLDER.remove(id);
        RETRY_HOLDER.remove(id);
    }
}
