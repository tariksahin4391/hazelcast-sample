package com.example.demo.config;

import com.example.demo.model.Constant;
import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
    @Bean
    public Config hzConfig(){
        EvictionConfig evictionConfig = new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE)
                //.setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                .setSize(3);//MB
        Config config = new Config()
                .setInstanceName(Constant.HZ_INSTANCE)
                .addMapConfig(new MapConfig()
                        .setName(Constant.MY_MODEL_MAP)
                        .setEvictionConfig(evictionConfig)
                        .setTimeToLiveSeconds(600)
                )
                .addListConfig(new ListConfig()
                        .setName(Constant.MY_MODEL_LIST)
                        .setMaxSize(1000));
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(4);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getJetConfig().setEnabled(true);
        //config.setIntegrityCheckerConfig(new IntegrityCheckerConfig().setEnabled(true));
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("localhost");
        return config;
    }
}
