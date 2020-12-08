package com.vocalink.crossproduct.ui.dto.alert;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertSearchParams implements Serializable {

  private final int offset = parseInt(getDefault(OFFSET));
  private final int limit = parseInt(getDefault(LIMIT));
  private List<String> priorities;
  private LocalDateTime dateFrom = LocalDateTime.now().minusDays(parseLong(getDefault(DAYS_LIMIT)));
  private LocalDateTime dateTo;
  private List<String> types;
  private List<String> entities;
  private String alertId;
  private List<String> sort;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDateTime.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDateTime.parse(dateTo);
  }
}