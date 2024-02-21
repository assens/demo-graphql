package com.foo.demo.graphql.controller;

import java.io.IOException;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.foo.demo.graphql.domain.FileUpload;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FileUploadGraphQLController {

  @MutationMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public FileUpload uploadFile(@Argument MultipartFile file) throws IOException {
    log.info("{} uploaded. Size: {} bytes.", file.getOriginalFilename(), file.getSize());
    return new FileUpload(file.getOriginalFilename(), file.getSize());
  }

}
