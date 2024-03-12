package com.joyjoin.registryservice;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogging implements CommandLineRunner {

    @Autowired
    private EurekaInstanceConfig eurekaInstanceConfig;
    @Override
    public void run(String... args) throws Exception {
        String defaultZone = eurekaInstanceConfig.getStatusPageUrl();
        System.out.println("Default Zone of registry-service: " + defaultZone);
    }
}
