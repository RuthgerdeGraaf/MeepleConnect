package com.meepleconnect.boardgamesapi.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final UserDetails userDetails;

    public JwtAuthenticationToken(String token, UserDetails userDetails,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
