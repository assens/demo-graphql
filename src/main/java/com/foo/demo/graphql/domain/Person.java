package com.foo.demo.graphql.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Person {
  private String firstName;
  private String lastName;
  private BigDecimal weight;
  private LocalDate birthday;
}
