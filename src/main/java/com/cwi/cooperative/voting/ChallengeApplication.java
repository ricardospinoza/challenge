package com.cwi.cooperative.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Tratamento de exceções do sistema
 * @author ricardo.spinoza
 *
 */
@SpringBootApplication
@EnableFeignClients
public class ChallengeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}
}
