package com.foo.demo.graphql.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.MediaType.APPLICATION_GRAPHQL_RESPONSE_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.foo.demo.graphql.domain.Person;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = MOCK)
@Slf4j
class PersonGraphQLContrllerTest extends AbstractGraphQLControllerTest {

  @Test
  void testWithhttpGraphQlTester() {
    // arrange
    final var graphQlTester = httpGraphQlTester();
    final var queryDoc = "query { persons { firstName lastName weight birthday } }";

    // act
    final var result = graphQlTester
        .document(queryDoc)
        .execute()
        .path("persons")
        .entityList(Person.class)
        .hasSizeGreaterThan(0)
        .hasSize(9);

    log.info("{}", result.get());
  }

  @Test
  void testWithWebTestClient() throws Exception {
    // arrange
    final var queryDoc = """
        { "query" : "{ persons { firstName lastName weight birthday } }"}
        """;

    webTestClient().post()
        .contentType(APPLICATION_JSON)
        .bodyValue(queryDoc).exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .value(jsonDataSourceString -> {
          final var jsonContext = JsonPath.parse(jsonDataSourceString);
          final var persons = jsonContext.read("$.data.persons", JSONArray.class);
          assertEquals(9, persons.size());
          log.info("{}", persons);
        });
  }

  @Test
  void testWithMockMvc() throws Exception {
    // arrange
    final var queryDoc = """
        { "query" : "{ persons { firstName lastName weight birthday } }"}
        """;
    final Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"));
    
    // act
    final var asyncMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(authorities))
        .contentType(APPLICATION_JSON)
        .content(queryDoc)
        .accept(APPLICATION_GRAPHQL_RESPONSE_VALUE))
        .andReturn();
    
    mockMvc.perform(asyncDispatch(asyncMvcResult))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.data.persons").exists());

  }
}
