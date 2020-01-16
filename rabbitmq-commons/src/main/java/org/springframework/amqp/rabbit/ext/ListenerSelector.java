package org.springframework.amqp.rabbit.ext;

/**
 * @ClassName ListenerSelector
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:59
 **/
public class ListenerSelector {
    public static final String multiThread = "multiThreadListenerContainerFactory";

    public static final String multiThreadAndManualAcknowledge =
            "multiThreadAndManualAcknowledgeListenerContainerFactory";

    public static final String singleThread = "singleThreadListenerContainerFactory";

    public static final String singleThreadAndManualAcknowledge =
            "singleThreadAndManualAcknowledgeListenerContainerFactory";

    public static final String multiThreadTo20 = "multiThreadTo20ListenerContainerFactory";

    public static final String multiThreadTo50 = "multiThreadTo50ListenerContainerFactory";
}
