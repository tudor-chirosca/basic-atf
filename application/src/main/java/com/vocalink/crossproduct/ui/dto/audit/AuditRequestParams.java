package com.vocalink.crossproduct.ui.dto.audit;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuditRequestParams {

  private int offset;
  @ValidLimit
  private int limit;
  @ValidFromDate
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateFrom;
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateTo;
  private String participant;
  private String user;
  private String responseContent;
  @Setter(AccessLevel.PRIVATE)
  private List<String> events;
  private List<String> sort;

  public void setDate_from(String dateFrom) {
    this.dateFrom = ZonedDateTime.of(LocalDate.parse(dateFrom), LocalTime.MIN, ZoneId.of("UTC"));
  }

  public void setDate_to(String dateTo) {
    this.dateTo = ZonedDateTime.of(LocalDate.parse(dateTo), LocalTime.MAX, ZoneId.of("UTC"));
  }

  public void setEvent_types(List<String> events) {
    this.events = events;
  }
}
