package com.foo.demo.graphql.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(HttpSecurity.class)
public class AuthorizationSecurityConfig {

  @Bean
  SecurityFilterChain apiFilterChain(final HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/**").permitAll()
        .requestMatchers("/graphiql/**").permitAll()
        .requestMatchers("/graphql/**").permitAll()
        .requestMatchers("/**").authenticated()
        );
    return http.build();
  }

}
