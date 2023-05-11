package com.example.demo.service.api;

import java.util.List;

public interface BaseCacheService {
    <T> T getFromMapCache(String mapName, String key);
    <T> boolean putToMapCache(String mapName, String key, String value, boolean overrideIfExisted, T t);
    <T> void removeFromMapCache(String mapName, String key);
    <T> void clearMapCache(String mapName);
    <T> void putToListCache(String listName, T t);
    <T> void putListToListCache(String listName, List<T> elems);
    <T> void removeElementFromListCache(String listName, T t);
    <T> void clearListCache(String listName);
    <T> List<T> getList(String listName);
}
