package com.vocalink.crossproduct.ui.dto.alert;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertSearchRequest implements Serializable {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> priorities;
  @ValidFromDate
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateFrom = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(parseLong(getDefault(DAYS_LIMIT)));
  @Setter(AccessLevel.PRIVATE)
  private ZonedDateTime dateTo;
  private List<String> types;
  private List<String> entities;
  @Setter(AccessLevel.PRIVATE)
  private String alertId;
  private List<String> sort;

  public void setDate_from(String dateFrom) {
    this.dateFrom = ZonedDateTime.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = ZonedDateTime.parse(dateTo);
  }

  public void setAlert_id(String alertId) {
    this.alertId = alertId;
  }
}
