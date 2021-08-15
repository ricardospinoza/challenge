package com.cwi.cooperative.voting.resources.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.cwi.cooperative.voting.resouces.response.CPFResponse;

@FeignClient(url = "${client.url.cpf-validator}", name="cpf-validator")
public interface CPFClient {
	@GetMapping( value = "/users/{cpf}", consumes = "application/json" )
	CPFResponse getCPF(@PathVariable String cpf);
}