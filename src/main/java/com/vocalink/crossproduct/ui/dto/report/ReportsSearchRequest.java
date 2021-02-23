package com.vocalink.crossproduct.ui.dto.report;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

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
  private String reportType;
  private String participant;
  private String id;
  @ValidFromDate
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateFrom = ZonedDateTime.now().minusDays(parseLong(getDefault(DAYS_LIMIT)));
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateTo;

  public void setDate_from(String dateFrom) {
    this.dateFrom = ZonedDateTime.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = ZonedDateTime.parse(dateTo);
  }
}
