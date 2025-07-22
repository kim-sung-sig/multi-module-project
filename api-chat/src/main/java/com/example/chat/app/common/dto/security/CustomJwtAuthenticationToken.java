package com.example.chat.app.common.dto.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Set;

@Getter
public class CustomJwtAuthenticationToken extends AbstractAuthenticationToken {

	private final Jwt jwt;
	private final String userId;

	public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> grantedAuthorities) {
		super(grantedAuthorities);
		this.jwt = jwt;
		this.userId = jwt.getClaimAsString("userId");
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return jwt.getTokenValue();
	}

	@Override
	public Object getPrincipal() {
		return userId;
	}

	public Set<String> authorities() {
		return AuthorityUtils.authorityListToSet(this.getAuthorities());
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException("setAuthenticated is unsupported");
	}

}
