package org.springframework.amqp.rabbit.ext.service;

import org.springframework.amqp.rabbit.ext.entity.ProduceEntity;
import org.springframework.amqp.rabbit.ext.dao.ProduceDao;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class ProduceService extends ServiceImpl<ProduceDao, ProduceEntity> {

}
