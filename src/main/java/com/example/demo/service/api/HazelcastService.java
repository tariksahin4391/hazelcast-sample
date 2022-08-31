package com.example.demo.service.api;

import com.example.demo.model.MyCacheModel;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface HazelcastService {
    MyCacheModel getFromCache(String cacheKey) throws JsonProcessingException;
    String putToCache(String key,String value);
    void putManyElementToCache();
    MyCacheModel getByCacheable(String key);
}
