package com.joyjoin.registryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableEurekaServer
public class RegistryServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(RegistryServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RegistryServiceApplication.class, args);
	}

	/**
	 * Is used to log if there are some new registrations, to have a better log info on AWS
	 * @param event
	 */
	@EventListener
	public void onInstanceRegistered(EurekaInstanceRegisteredEvent event) {
		logger.info("Service registered with Eureka - ServiceId: {}, InstanceId: {}, Host: {}, Port: {}",
				event.getInstanceInfo().getAppName(),
				event.getInstanceInfo().getInstanceId(),
				event.getInstanceInfo().getHostName(),
				event.getInstanceInfo().getPort());
	}

	@EventListener
	public void onInstanceRenewed(EurekaInstanceRenewedEvent event) {
		logger.debug("Service renewed registration with Eureka - InstanceId: {}", event.getInstanceInfo().getInstanceId());
	}

}

