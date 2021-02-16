package com.vocalink.crossproduct.infrastructure.bps.report;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(NON_NULL)
public class BPSReportSearchRequest {

  private int offset;
  private int limit;
  private List<String> sort;
}
