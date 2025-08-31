package com.example.user.app.application.auth.dto.request;

import com.example.common.config.trim.NoTrim;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
		@NotBlank String username,

		@NoTrim
		@NotBlank String password
) {}