package org.springframework.amqp.rabbit.ext.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("produce_entity")
public class ProduceEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;
}
