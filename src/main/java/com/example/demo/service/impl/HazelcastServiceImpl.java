package com.example.demo.service.impl;

import com.example.demo.model.Constant;
import com.example.demo.model.MyCacheModel;
import com.example.demo.service.api.HazelcastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HazelcastServiceImpl implements HazelcastService {

    @Override
    public MyCacheModel getFromCache(String cacheKey) throws JsonProcessingException {
        HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName(Constant.HZ_INSTANCE);
        IMap<String, MyCacheModel> cacheMap = hazelcastClient.getMap(Constant.MY_MODEL_MAP);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] arr = objectMapper.writeValueAsBytes(cacheMap);
        System.out.println("size : "+arr.length);
        MyCacheModel myCacheModel = cacheMap.get(cacheKey);
        if (myCacheModel == null)
            throw new RuntimeException("not found");
        return cacheMap.get(cacheKey);
        /*IList<MyCacheModel> cacheList = hazelcastClient.getList("my-model");
        List<MyCacheModel> myCacheModels = cacheList.stream().filter(m -> m.getName().equals(cacheKey)).toList();
        if(myCacheModels.isEmpty())
            throw new RuntimeException("not found");
        return myCacheModels.get(0);

         */
    }

    @Override
    public String putToCache(String key, String value) {
        HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName(Constant.HZ_INSTANCE);
        IMap<String, MyCacheModel> cacheMap = hazelcastClient.getMap(Constant.MY_MODEL_MAP);
        MyCacheModel myCacheModel = cacheMap.get(key);
        if (myCacheModel == null)
            cacheMap.put(key, new MyCacheModel(value));
        else
            cacheMap.replace(key, new MyCacheModel(value));
        /*IList<MyCacheModel> cacheList = hazelcastClient.getList("my-model");
        List<MyCacheModel> myCacheModels = cacheList.stream().filter(m -> m.getName().equals(key)).toList();
        if(myCacheModels.isEmpty())
            cacheList.add(new MyCacheModel(key));
        else {
            cacheList.remove(myCacheModels.get(0));
            cacheList.add(new MyCacheModel(key));
        }

         */
        return "OK";
    }

    public void putManyElementToCache() {
        for (int i = 0; i < 200000; i++) {
            putToCache("key-" + i, "value" + i);
        }
    }

    /*
    * Spring boot cache mekanizması
    * spring kendi içinde in-memory cache tutar.hazelcast e kaydedilmez
    * fonksiyon aynı key değeri ile ikinci kez çağrıldığında doğrudan sonuç döner.
    * aradaki logların basılmamasından anlaşılabilir
    * */
    @Cacheable(Constant.MY_MODEL_MAP)
    public MyCacheModel getByCacheable(String key) {
        log.info("creating cache object");
        MyCacheModel cacheModel = new MyCacheModel();
        cacheModel.setName("cache model "+key);
        log.info("created object");
        return cacheModel;
    }
}
