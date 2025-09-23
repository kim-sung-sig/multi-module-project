package com.example.user.app.application.auth.dto.request;

import com.example.common.anotations.NoTrim;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
		@NotBlank String username,

		@NoTrim
		@NotBlank String password
) {}