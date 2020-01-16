package org.springframework.amqp.rabbit.ext;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName MessageWrap
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:00
 **/
@Getter
@Setter
public class MessageWrap<T> implements Serializable {
    private static final long serialVersionUID = -4248141006365855376L;
    private String project;
    private String address;
    private Integer retries;
    private T message;

    public MessageWrap(T message) {
        this.message = message;
    }

    public MessageWrap(int retries, T message) {
        this.retries = Integer.valueOf(retries);
        this.message = message;
    }

    public MessageWrap(String project, String address, int retries, T message) {
        this.project = project;
        this.address = address;
        this.retries = Integer.valueOf(retries);
        this.message = message;
    }
}
