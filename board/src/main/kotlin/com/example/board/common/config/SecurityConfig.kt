package com.example.board.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
class SecurityConfig (
	private val whiteList : Array<String> = arrayOf(
		"/v3/api-docs/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/webjars/**",
		"/favicon.ico"
	)
){

	@Bean
	fun filterChain(http : HttpSecurity) : SecurityFilterChain {
		http
			.csrf { it.disable() }
			.authorizeHttpRequests { auth ->
				auth
					.requestMatchers(*whiteList).permitAll()
					.anyRequest().authenticated()
			}
			.formLogin { it.disable() }
			.logout { it.disable() }
			.httpBasic { it.disable() }

		return http.build()
	}

}