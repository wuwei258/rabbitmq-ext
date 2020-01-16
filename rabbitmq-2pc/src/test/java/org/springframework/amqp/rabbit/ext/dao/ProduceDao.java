package org.springframework.amqp.rabbit.ext.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.amqp.rabbit.ext.entity.ProduceEntity;

@DS("db")
public interface ProduceDao extends BaseMapper<ProduceEntity> {
}
