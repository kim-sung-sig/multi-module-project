package com.example.user.app.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiTestDto {

	private List<String> params;

	private List<TestEnum> enums;

	enum TestEnum {
		ONE, TWO, THREE
	}
}