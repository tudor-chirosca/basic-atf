package com.vocalink.crossproduct.ui.dto.audit;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuditRequestParams {

  @ValidFromDate
  @Setter(AccessLevel.PRIVATE)
  private LocalDate dateFrom;
  @Setter(AccessLevel.PRIVATE)
  private LocalDate dateTo;
  private String participant;
  private String user;
  private String event;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDate.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDate.parse(dateTo);
  }
}
