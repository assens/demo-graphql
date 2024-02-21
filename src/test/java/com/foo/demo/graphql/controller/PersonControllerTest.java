package com.foo.demo.graphql.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PersonControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void test() throws Exception {
    mockMvc.perform(get("/api/person")
        .with(jwt()
            .authorities(new SimpleGrantedAuthority("ADMIN"))
            .jwt(jwt -> jwt.claim("oid", UUID.randomUUID().toString()))))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

}
