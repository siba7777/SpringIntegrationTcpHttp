package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@RequestMapping("/echo")
	public String index() {
		return "Hello World!";
	}
	
	@PostMapping("/echo")
	public String post(@RequestBody String body) {
		return "Web API Response "+body;
	}

}
