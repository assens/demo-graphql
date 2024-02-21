package com.foo.demo.graphql.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.foo.demo.graphql.domain.Person;
import com.github.javafaker.Faker;

@Service
public class PersonService {

  private Faker faker = new Faker();
  

  public Collection<Person> findAll() {
     
    final var result = new ArrayList<Person>();
    IntStream.range(1, 10).forEach(i -> {
      final var age = BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(100));
      final var birthdate =  LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault());
      result.add(new Person(faker.name().firstName(), faker.name().lastName(), age, birthdate));
    });
    return result;
  }
}
