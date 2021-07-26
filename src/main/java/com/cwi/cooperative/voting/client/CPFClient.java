package com.cwi.cooperative.voting.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cwi.cooperative.voting.response.CPFResponse;

@FeignClient(url = "${client.url.cpf-validator}", name="cpf-validator")
public interface CPFClient {
	@GetMapping("/users/{cpf}")
	public CPFResponse getCPF(@PathVariable String cpf);
}
