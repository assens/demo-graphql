package com.foo.demo.graphql.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcHttpConnector;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import name.nkonev.multipart.spring.graphql.client.MultipartGraphQlWebClient;

public abstract class AbstractGraphQLControllerTest {
  private static final Collection<GrantedAuthority> DEFAULT_AUTHORITIES = List.of(new SimpleGrantedAuthority("ADMIN"));
  private static final Map<String, Object> DEFAULT_CLAIMS = Map.of("oid", UUID.randomUUID().toString());

  @Autowired
  MockMvc mockMvc;
  
  @Autowired
  protected MultipartGraphQlWebClient multipartGraphQlWebClient;

  protected WebTestClient webTestClient() {
    return webTestClient(DEFAULT_CLAIMS, DEFAULT_AUTHORITIES);
  }

  protected HttpGraphQlTester httpGraphQlTester() {
    return httpGraphQlTester(DEFAULT_CLAIMS, DEFAULT_AUTHORITIES);
  }

  protected HttpGraphQlTester httpGraphQlTester(final Map<String, Object> claims, final Collection<GrantedAuthority> authorities) {
    final WebTestClient webTestClient = webTestClient(claims, authorities);
    return HttpGraphQlTester.create(webTestClient);
  }

  private WebTestClient webTestClient(final Map<String, Object> claims, final Collection<GrantedAuthority> authorities) {
    final WebTestClient webTestClient = MockMvcWebTestClient.bindTo(mockMvc)
        .baseUrl("/graphql")
        .build()
        .mutateWith(jwtWebTestClientConfigurer(claims, authorities));
    return webTestClient;
  }

  private WebTestClientConfigurer jwtWebTestClientConfigurer(final Map<String, Object> claims, final Collection<GrantedAuthority> authorities) {
    return (builder, httpHandlerBuilder, connector) -> {
      if (connector instanceof MockMvcHttpConnector mockMvcConnector) {
        final var jwtRequestPostProcessor = jwt()
            .authorities(authorities)
            .jwt(jwt -> jwt.claims(c -> c.putAll(claims)));
        builder.clientConnector(mockMvcConnector.with(List.of(jwtRequestPostProcessor)));
      }
    };
  }
}
