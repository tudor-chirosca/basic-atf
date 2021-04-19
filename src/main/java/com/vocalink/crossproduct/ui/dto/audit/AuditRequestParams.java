package com.vocalink.crossproduct.ui.dto.audit;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import java.time.LocalDate;
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
  private LocalDate dateFrom;
  @Setter(AccessLevel.PRIVATE)
  private LocalDate dateTo;
  private String participant;
  private String user;
  @Setter(AccessLevel.PRIVATE)
  private List<String> events;
  private List<String> sort;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDate.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDate.parse(dateTo);
  }

  public void setEvent_types(List<String> events) {
    this.events = events;
  }
}
