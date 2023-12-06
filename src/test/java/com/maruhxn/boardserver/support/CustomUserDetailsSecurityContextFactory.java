package com.maruhxn.boardserver.support;

import com.maruhxn.boardserver.auth.common.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomUserDetailsSecurityContextFactory implements WithSecurityContextFactory<CustomWithUserDetails> {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public SecurityContext createSecurityContext(CustomWithUserDetails withUser) {
        String email = withUser.email();
        String password = withUser.password();
        AjaxAuthenticationToken authRequest = AjaxAuthenticationToken.unauthenticated(email, password);
        AjaxAuthenticationToken authentication = (AjaxAuthenticationToken) authenticationProvider.authenticate(authRequest);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
