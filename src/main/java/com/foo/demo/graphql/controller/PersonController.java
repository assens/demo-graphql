package com.foo.demo.graphql.controller;

import java.util.Collection;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foo.demo.graphql.domain.Person;
import com.foo.demo.graphql.service.PersonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {

  private final PersonService personService;

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public Collection<Person> findAll(final Authentication authentication) {
    log.info("{}", authentication);
    return personService.findAll();
  }
}
