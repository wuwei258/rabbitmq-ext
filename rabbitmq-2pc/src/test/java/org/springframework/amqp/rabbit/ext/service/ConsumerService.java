package org.springframework.amqp.rabbit.ext.service;

import org.springframework.amqp.rabbit.ext.dao.ConsumerDao;
import org.springframework.amqp.rabbit.ext.entity.ConsumerEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class ConsumerService extends ServiceImpl<ConsumerDao, ConsumerEntity> {}
