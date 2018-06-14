package com.canary.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.canary.finance.config.RemoteProperties;
import com.canary.finance.remote.netty.RemoteServer;

@SpringBootApplication
@EnableConfigurationProperties(RemoteProperties.class)
public class RemoteApplication implements CommandLineRunner {
	@Autowired
	private RemoteProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(RemoteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		RemoteServer server = new RemoteServer(this.properties);
		server.start();
	}
}