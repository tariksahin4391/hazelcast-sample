package com.example.demo.service.impl;

import com.example.demo.model.Constant;
import com.example.demo.model.MyCacheModel;
import com.example.demo.service.api.BaseCacheService;
import com.example.demo.service.api.HazelcastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.collection.IList;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HazelcastServiceImpl implements HazelcastService {

    @Autowired
    BaseCacheService baseCacheService;

    @Override
    public MyCacheModel getFromCache(String cacheKey) throws JsonProcessingException {
        /*HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName(Constant.HZ_INSTANCE);
        IMap<String, MyCacheModel> cacheMap = hazelcastClient.getMap(Constant.MY_MODEL_MAP);
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] arr = objectMapper.writeValueAsBytes(cacheMap);
        System.out.println("size : "+arr.length);

         */
        MyCacheModel myCacheModel = baseCacheService.getFromMapCache(Constant.MY_MODEL_MAP,cacheKey);
        if (myCacheModel == null)
            throw new RuntimeException("not found");
        return myCacheModel;
        /*IList<MyCacheModel> cacheList = hazelcastClient.getList("my-model");
        List<MyCacheModel> myCacheModels = cacheList.stream().filter(m -> m.getName().equals(cacheKey)).toList();
        if(myCacheModels.isEmpty())
            throw new RuntimeException("not found");
        return myCacheModels.get(0);

         */
    }

    @Override
    public String putToCache(String key, String value) {
        baseCacheService.putToMapCache(Constant.MY_MODEL_MAP,key,value,true,new MyCacheModel(value,new ArrayList<>()));
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

    @Override
    public String putToCacheMapList(String key, String value) {
        HazelcastInstance hazelcastClient = Hazelcast.getHazelcastInstanceByName(Constant.HZ_INSTANCE);
        IMap<String, MyCacheModel> cacheMap = hazelcastClient.getMap(Constant.MY_MODEL_MAP);
        MyCacheModel myCacheModel = cacheMap.get(key);
        if (myCacheModel == null)
            cacheMap.put(key, new MyCacheModel("mapForList", List.of(value)));
        else {
            MyCacheModel newModel = new MyCacheModel();
            newModel.setName(myCacheModel.getName());
            List<String> newModels = new ArrayList<>(myCacheModel.getModels());
            newModels.add(value);
            newModel.setModels(newModels);
            cacheMap.replace(key,newModel);
        }
        return "OK";
    }

    public String addToListCache(String value) {
        MyCacheModel myCacheModel = new MyCacheModel();
        myCacheModel.setName(value);
        myCacheModel.setModels(List.of(value));
        baseCacheService.putToListCache(Constant.MY_MODEL_LIST,myCacheModel);
        baseCacheService.putListToListCache(Constant.MY_MODEL_LIST,List.of(myCacheModel,myCacheModel));
        return "OK";
    }

    public List<MyCacheModel> getListCache() {
        return baseCacheService.getList(Constant.MY_MODEL_LIST);
    }

    public String removeElementFromList(String value) {
        MyCacheModel myCacheModel = (MyCacheModel) baseCacheService.getList(Constant.MY_MODEL_LIST).stream().filter(s -> ((MyCacheModel) s).getName().equals(value)).toList().get(0);
        baseCacheService.removeElementFromListCache(Constant.MY_MODEL_LIST,myCacheModel);
        return "OK";
    }

    public String clearAllCache() {
        baseCacheService.clearMapCache(Constant.MY_MODEL_MAP);
        baseCacheService.clearListCache(Constant.MY_MODEL_LIST);
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
