package org.springframework.amqp.rabbit.ext.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.amqp.rabbit.ext.entity.ConsumerEntity;

@DS("db2")
public interface ConsumerDao extends BaseMapper<ConsumerEntity> {
}
