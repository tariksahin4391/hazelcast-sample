package com.example.demo.service.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        value = "base.cache.useDefaultHazelcast",
        havingValue = "Y"
)
public class CacheServiceImpl extends AbstractCacheService {
}
