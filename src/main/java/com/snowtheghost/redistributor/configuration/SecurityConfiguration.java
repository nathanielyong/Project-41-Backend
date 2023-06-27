package com.snowtheghost.redistributor.configuration;

import com.snowtheghost.redistributor.infrastructure.authentication.JwtAuthenticationFilter;
import com.snowtheghost.redistributor.infrastructure.authentication.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtTokenProvider tokenProvider;

    public SecurityConfiguration(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/users/register").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/payment/stripe/checkout/handle/*").permitAll()
                .requestMatchers(HttpMethod.OPTIONS).permitAll() // Allow preflight requests
                .requestMatchers("/error").permitAll()
                .requestMatchers("/**").authenticated());
        return http.build();
    }
}