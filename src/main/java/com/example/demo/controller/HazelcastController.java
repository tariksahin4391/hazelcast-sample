package com.example.demo.controller;

import com.example.demo.model.MyCacheModel;
import com.example.demo.service.api.HazelcastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HazelcastController {

    @Autowired
    HazelcastService hazelcastService;

    @GetMapping("/get-from-cache/{cacheKey}")
    public ResponseEntity<MyCacheModel> getFromCache(@PathVariable String cacheKey) throws JsonProcessingException {
        return ResponseEntity.status(200).body(hazelcastService.getFromCache(cacheKey));
    }

    @GetMapping("/put-to-cache/{key}/{value}")
    public ResponseEntity<String> putToCache(@PathVariable String key,@PathVariable String value){
        return ResponseEntity.status(200).body(hazelcastService.putToCache(key,value));
    }

    @GetMapping("/put-to-map-list/{key}/{value}")
    public ResponseEntity<String> putToMapList(@PathVariable String key, @PathVariable String value) {
        return ResponseEntity.status(200).body(hazelcastService.putToCacheMapList(key, value));
    }

    @GetMapping("/put-to-list/{value}")
    public ResponseEntity<String> putToList(@PathVariable String value) {
        return ResponseEntity.status(200).body(hazelcastService.addToListCache(value));
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<MyCacheModel>> getList() {
        return ResponseEntity.status(200).body(hazelcastService.getListCache());
    }

    @GetMapping("/remove-from-list/{value}")
    public ResponseEntity<String> removeFromList(@PathVariable String value) {
        return ResponseEntity.status(200).body(hazelcastService.removeElementFromList(value));
    }

    @GetMapping("/clear")
    public ResponseEntity<String> clear() {
        hazelcastService.clearAllCache();
        return ResponseEntity.status(200).body("OK");
    }

    @GetMapping("/put-many-elem")
    public ResponseEntity<String> putManyElem() {
        hazelcastService.putManyElementToCache();
        return ResponseEntity.status(200).body("OK");
    }

    @GetMapping("/get-by-cacheable/{key}")
    public ResponseEntity<MyCacheModel> getByCacheable(@PathVariable("key") String key) {
        return ResponseEntity.status(200).body(hazelcastService.getByCacheable(key));
    }
}
