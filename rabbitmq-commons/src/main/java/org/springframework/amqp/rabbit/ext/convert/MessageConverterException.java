package org.springframework.amqp.rabbit.ext.convert;

/**
 * @ClassName MessageConverterException
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:03
 **/
public class MessageConverterException extends Exception {
    private static final long serialVersionUID = 1276761046865444676L;

    public MessageConverterException(String errorMsg) {
        super(errorMsg);
    }

    public MessageConverterException(String format, Object... params) {
        super(String.format(format, params));
    }

    public MessageConverterException(Throwable e) {
        super(e);
    }

    public MessageConverterException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }
}
