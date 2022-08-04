package com.discover.discoverapi.config;

import com.discover.discoverapi.filters.JWTTokenGeneratorFilter;
import com.discover.discoverapi.filters.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // gets custom authentication entry point
        AuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

        // configuration to use stateless authentication
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        // configuration for cors management
        http = http.cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setExposedHeaders(Arrays.asList("Authorization"));
            config.setMaxAge(3600L);
            return config;
        }).and();

        // disables csrf
        http = http.csrf().disable();

        // define filters
        http = http
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);

        // protects every request
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/albums/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/albums/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/albums").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/tracks/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/tracks/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/tracks").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/artists/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/artists/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/artists").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/genres/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/genres/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/genres").hasAuthority("ADMIN")
                .and()
                .httpBasic()
                .authenticationEntryPoint(entryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
