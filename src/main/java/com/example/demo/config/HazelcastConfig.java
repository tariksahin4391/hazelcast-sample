package com.example.demo.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(
        value = "base.cache.useDefaultHazelcast",
        havingValue = "Y"
)
public class HazelcastConfig {
    @Value("${base.cache.mapNames}")
    private String mapNames;

    @Value("${base.cache.mapSizes}")
    private String mapSizes;

    @Value("${base.cache.mapTtls}")
    private String mapTtls;

    @Value("${base.cache.listNames}")
    private String listNames;

    @Value("${base.cache.listSizes}")
    private String listSizes;

    @Value("${base.cache.portCount}")
    private int portCount;

    @Value("${base.cache.instances}")
    private String instances;

    @Value("${base.cache.hzInstanceName}")
    private String hzInstanceName;

    @Bean
    public Config hzConfig(){
        String[] mapNameArr = mapNames.split(",");
        String[] mapSizeArr = mapSizes.split(",");
        String[] mapTtlArr = mapTtls.split(",");
        Config config = new Config()
                .setInstanceName(hzInstanceName);

        for (int i=0;i<mapNameArr.length;i++) {
            EvictionConfig evictionConfig = new EvictionConfig()
                    .setEvictionPolicy(EvictionPolicy.LRU)
                    .setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE)
                    //.setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                    .setSize(Integer.decode(mapSizeArr[i]));//MB
            config.addMapConfig(new MapConfig()
                    .setName(mapNameArr[i])
                    .setEvictionConfig(evictionConfig)
                    .setTimeToLiveSeconds(Integer.parseInt(mapTtlArr[i]))
            );
        }

        String[] listNameArr = listNames.split(",");
        String[] listSizeArr = listSizes.split(",");

        for (int i=0;i< listNameArr.length;i++) {
            config.addListConfig(new ListConfig()
                    .setName(listNameArr[i])
                    .setMaxSize(Integer.decode(listSizeArr[i]))
            );
        }
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(portCount);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getJetConfig().setEnabled(true);
        //config.setIntegrityCheckerConfig(new IntegrityCheckerConfig().setEnabled(true));
        String[] instanceArr = instances.split(",");
        for (String instance : instanceArr) {
            log.info("{} hz instance added", instance);
            config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(instance);
        }
        return config;
    }
}
