package com.example.user.app.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/board/test")
public class ApiTest {

	@GetMapping
	public String test(
			@ModelAttribute ApiTestDto dto
	) {
		return "API TEST";
	}
}