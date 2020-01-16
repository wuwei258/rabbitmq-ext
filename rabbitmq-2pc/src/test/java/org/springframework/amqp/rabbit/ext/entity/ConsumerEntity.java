package org.springframework.amqp.rabbit.ext.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("consumer_entity")
public class ConsumerEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;
}
