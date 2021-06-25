package com.vocalink.crossproduct.ui.dto.report;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportsSearchRequest {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> sort;
  private List<String> reportTypes;
  private List<String> participants;
  private String id;
  @ValidFromDate
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateFrom;
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateTo;

  public void setDate_from(String dateFrom) {
    this.dateFrom = ZonedDateTime.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = ZonedDateTime.parse(dateTo);
  }

  public void setReport_types(List<String> reportTypes) {
    this.reportTypes = reportTypes;
  }
}
