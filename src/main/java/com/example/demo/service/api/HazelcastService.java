package com.example.demo.service.api;

import com.example.demo.model.MyCacheModel;

public interface HazelcastService {
    MyCacheModel getFromCache(String cacheKey);

    String putToCache(String key,String value);
}
