package com.example.demo.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
    @Bean
    public Config hzConfig(){
        EvictionConfig evictionConfig = new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                .setSize(300);
        Config config = new Config()
                .setInstanceName("hz-cache")
                .addMapConfig(new MapConfig()
                        .setName("my-model")
                        .setEvictionConfig(evictionConfig)
                        .setTimeToLiveSeconds(20)
                );
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(4);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost");
        return config;
    }
}
