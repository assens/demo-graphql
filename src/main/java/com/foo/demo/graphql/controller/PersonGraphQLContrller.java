package com.foo.demo.graphql.controller;

import java.util.Collection;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.foo.demo.graphql.domain.Person;
import com.foo.demo.graphql.service.PersonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PersonGraphQLContrller {

  private final PersonService personService;

  @GetMapping
  @PreAuthorize("hasAnyAuthority('ADMIN', 'TEST', 'APPROLE_e2478b77-25af-4f72-bd33-5d79fdb575cb')")
  @QueryMapping
  public Collection<Person> persons(final Authentication authentication) {
    log.info("{}", authentication);
    return personService.findAll();
  }

}
