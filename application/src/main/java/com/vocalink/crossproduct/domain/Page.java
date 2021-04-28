package com.vocalink.crossproduct.domain;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Page<T> implements Serializable {

  private final int totalResults;
  private final List<T> items;
}
