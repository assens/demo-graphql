package com.foo.demo.graphql.controller;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = MOCK)
class FileUploadGraphQLControllerTest extends AbstractGraphQLControllerTest {

  @Test
  void testWithMockMvc() throws Exception {

    // arrange
    final var fileName = "fileName.xlsx";
    final var fileByteArray = new byte[100];

    final var operations = """
        { "query" : "mutation FileUpload($file: Upload!) { uploadFile(file: $file) { fileName size } }" }
        """;

    final var variables = """
        { "file" : ["variables.file"] }
        """;

    final var filePart = new MockMultipartFile("file", fileName, "application/octet-stream", fileByteArray);
    final var operationsPart = new MockPart("operations", operations.getBytes());
    final var variablesPart = new MockPart("map", variables.getBytes());

    // act & assert
    final var asyncMvcResult = mockMvc.perform(MockMvcRequestBuilders
        .multipart("/graphql")
        .file(filePart)
        .part(operationsPart)
        .part(variablesPart)
        .accept(MediaType.APPLICATION_GRAPHQL_RESPONSE_VALUE)
        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
        .andReturn();

    mockMvc.perform(asyncDispatch(asyncMvcResult))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.uploadFile.fileName").value(fileName));

  }

}
