package org.folio.modusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ModUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModUsersApplication.class, args);
	}

}
