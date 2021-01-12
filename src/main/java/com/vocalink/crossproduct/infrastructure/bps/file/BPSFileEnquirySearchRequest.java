package com.vocalink.crossproduct.infrastructure.bps.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSFileEnquirySearchRequest {

  private final int offset;

  private final int limit;

  private final List<String> sort;

  @JsonProperty(value = "date_from")
  private final LocalDate dateFrom;

  @JsonProperty(value = "date_to")
  private final LocalDate dateTo;

  @JsonProperty(value = "cycle_ids")
  private final List<String> cycleIds;

  @JsonProperty(value = "msg_direction")
  private final String messageDirection;

  @JsonProperty(value = "msg_type")
  private final String messageType;

  @JsonProperty(value = "send_bic")
  private final String sendingBic;

  @JsonProperty(value = "recv_bic")
  private final String receivingBic;

  private final String status;

  @JsonProperty(value = "reason_code")
  private final String reasonCode;

  private final String id;
}
