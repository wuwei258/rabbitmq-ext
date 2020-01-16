package org.springframework.amqp.rabbit.ext.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @ClassName RabbitProperties
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 14:56
 **/
@Configuration
@ConfigurationProperties(prefix = "mq")
public class RabbitMqProperties implements Serializable {
    private static final long serialVersionUID = -2564370352142491756L;
    private String host;
    private String vhost;
    private String port;
    private String username;
    private String password;
    private String defaultExchange = "DEFAULT_EXCHANGE";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultExchange() {
        return defaultExchange;
    }

    public void setDefaultExchange(String defaultExchange) {
        this.defaultExchange = defaultExchange;
    }
}
