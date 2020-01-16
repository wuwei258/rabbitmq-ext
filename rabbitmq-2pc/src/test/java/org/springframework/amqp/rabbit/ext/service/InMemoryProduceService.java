package org.springframework.amqp.rabbit.ext.service;

import org.springframework.amqp.rabbit.ext.entity.ProduceEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName InMemoryProduceService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:28
 **/
@Service
public class InMemoryProduceService {
    private static final Map<Long, ProduceEntity> DATA_BASE = new ConcurrentHashMap<>();

    public void save(ProduceEntity produceEntity) {
        DATA_BASE.put(produceEntity.getId(), produceEntity);
    }
}
