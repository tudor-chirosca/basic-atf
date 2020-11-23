package com.vocalink.crossproduct.ui.dto.alert;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.ORDER;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.SORT;
import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;


@Getter
public class AlertSearchRequest implements Serializable {

  private final int offset;
  private final int limit;
  private final List<String> priorities;
  private final LocalDateTime dateFrom;
  private final LocalDateTime dateTo;
  private final List<String> alertTypes;
  private final List<String> entities;
  private final String alertId;
  private final String sort;
  private final String order;

  @JsonCreator
  public AlertSearchRequest(Integer offset, Integer limit,
      List<String> priorities, LocalDateTime dateFrom, LocalDateTime dateTo,
      List<String> alertTypes, List<String> entities, String alertId, String sort,
      String order) {
    this.offset = offset == null ? parseInt(getDefault(OFFSET)) : offset;
    this.limit = limit == null ? parseInt(getDefault(LIMIT)) : limit;
    this.priorities = priorities;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.alertTypes = alertTypes;
    this.entities = entities;
    this.alertId = alertId;
    this.sort = sort == null ? getDefault(SORT) : sort;
    this.order = order == null ? getDefault(ORDER) : order;
  }
}
