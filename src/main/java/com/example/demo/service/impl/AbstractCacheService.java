package com.example.demo.service.impl;

import com.example.demo.service.api.BaseCacheService;
import com.hazelcast.collection.IList;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
public abstract class AbstractCacheService implements BaseCacheService {

    @Value("${base.cache.hzInstanceName}")
    private String hzInstanceName;

    private HazelcastInstance hazelcastClient;

    @PostConstruct
    private void init() {
        Runnable runnable = () -> {
            hazelcastClient = Hazelcast.getHazelcastInstanceByName(hzInstanceName);
            while (hazelcastClient == null) {
                try {
                    log.info("hz client is null.trying again {}", hzInstanceName);
                    Thread.sleep(2000);
                    hazelcastClient = Hazelcast.getHazelcastInstanceByName(hzInstanceName);
                } catch (Throwable t) {
                    log.error("hz cache creation error", t);
                    break;
                }
            }
            if (hazelcastClient != null)
                log.info("hz client is OK");
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public <T> T getFromMapCache(String mapName, String key) {
        IMap<String, T> cacheMap = hazelcastClient.getMap(mapName);
        return cacheMap.get(key);
    }

    @Override
    public <T> boolean putToMapCache(String mapName, String key, String value, boolean overrideIfExisted, T t) {
        IMap<String, T> cacheMap = hazelcastClient.getMap(mapName);
        if (!cacheMap.containsKey(key)) {
            cacheMap.put(key, t);
            return true;
        }
        if (!overrideIfExisted)
            return false;
        cacheMap.replace(key,t);
        return true;
    }

    @Override
    public <T> void removeFromMapCache(String mapName, String key) {
        IMap<String, T> cacheMap = hazelcastClient.getMap(mapName);
        cacheMap.remove(key);
    }

    @Override
    public <T> void clearMapCache(String mapName) {
        IMap<String, T> cacheMap = hazelcastClient.getMap(mapName);
        cacheMap.clear();
    }

    @Override
    public <T> void putToListCache(String listName, T t) {
        IList<T> list = hazelcastClient.getList(listName);
        list.add(t);
    }

    @Override
    public <T> void putListToListCache(String listName, List<T> elems) {
        IList<T> list = hazelcastClient.getList(listName);
        list.addAll(elems);
    }

    @Override
    public <T> void removeElementFromListCache(String listName, T t) {
        IList<T> list = hazelcastClient.getList(listName);
        list.remove(t);
    }

    @Override
    public <T> void clearListCache(String listName) {
        IList<T> list = hazelcastClient.getList(listName);
        list.clear();
    }

    @Override
    public <T> List<T> getList(String listName) {
        return hazelcastClient.getList(listName);
    }
}
