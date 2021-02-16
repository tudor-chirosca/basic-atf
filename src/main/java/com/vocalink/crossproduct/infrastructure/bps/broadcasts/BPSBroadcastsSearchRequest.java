package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BPSBroadcastsSearchRequest {

  private int offset;
  private int limit;
  private List<String> sort;
  private String recipient;
  private String msg;
  private String id;
  private ZonedDateTime dateFrom;
  private ZonedDateTime dateTo;
}
