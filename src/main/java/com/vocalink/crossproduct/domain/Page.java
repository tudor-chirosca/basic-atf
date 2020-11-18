package com.vocalink.crossproduct.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Page<T> {

  private final int totalResults;
  private final List<T> items;

}
