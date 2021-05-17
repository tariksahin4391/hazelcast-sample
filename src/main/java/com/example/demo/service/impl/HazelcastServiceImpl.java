package com.example.demo.service.impl;

import com.example.demo.model.MyCacheModel;
import com.example.demo.service.api.HazelcastService;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Component;

@Component
public class HazelcastServiceImpl implements HazelcastService {

    @Override
    public MyCacheModel getFromCache(String cacheKey){
        HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName("hz-cache");
        IMap<String,MyCacheModel> cacheMap = hazelcastClient.getMap("my-model");
        return cacheMap.get(cacheKey);
    }

    @Override
    public String putToCache(String key,String value){
        HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName("hz-cache");
        IMap<String,MyCacheModel> cacheMap = hazelcastClient.getMap("my-model");
        MyCacheModel myCacheModel = cacheMap.get(key);
        if(myCacheModel == null)
            cacheMap.put(key,new MyCacheModel(value));
        else
            cacheMap.replace(key,new MyCacheModel(value));
        return "OK";
    }
}
