package com.vocalink.crossproduct.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Participant {

  private String id;
  private String bic;
  private String name;
  private String status;
  private String suspendedTime;
}


