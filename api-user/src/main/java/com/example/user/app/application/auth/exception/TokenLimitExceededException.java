package com.example.user.app.application.auth.exception;

import com.example.user.app.application.auth.domain.RefreshToken;
import lombok.Getter;

import java.util.Collection;

@Getter
public class TokenLimitExceededException extends Throwable {

    private final Collection<RefreshToken> existingTokens;

    public TokenLimitExceededException(Collection<RefreshToken> existingTokens) {
        super("Token limit exceeded. Existing tokens: " + existingTokens.size());
        this.existingTokens = existingTokens;
    }
}
