package com.foo.demo.graphql.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class AuthorizationSecurityConfig {

  @Bean
  SecurityFilterChain apiFilterChain(final HttpSecurity http) throws Exception {
    http
        .with(AadResourceServerHttpSecurityConfigurer.aadResourceServer(), withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/graphiql/**").permitAll()
            .requestMatchers("/**").authenticated());
    return http.build();
  }

}
