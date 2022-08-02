package com.discover.discoverapi.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// custom entry point to suppress default login modal
// appearing in the browser
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    public CustomAuthenticationEntryPoint() {
        this.setRealmName("myown");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // setting the WWW-Authenticate to something other than the default value
        // makes the login window not appear
        response.setHeader("WWW-Authenticate", "myown");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
