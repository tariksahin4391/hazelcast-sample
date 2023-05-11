package com.example.demo.service.api;

import com.example.demo.model.MyCacheModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface HazelcastService {
    MyCacheModel getFromCache(String cacheKey) throws JsonProcessingException;
    String putToCache(String key,String value);
    void putManyElementToCache();
    MyCacheModel getByCacheable(String key);
    String putToCacheMapList(String key, String value);
    String addToListCache(String value);
    List<MyCacheModel> getListCache();
    String removeElementFromList(String value);
    String clearAllCache();
}
