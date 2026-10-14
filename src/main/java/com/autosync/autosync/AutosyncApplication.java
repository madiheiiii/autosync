package com.autosync.autosync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AutosyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutosyncApplication.class, args);
	}

}
