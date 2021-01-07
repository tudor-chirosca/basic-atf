package com.vocalink.crossproduct.ui.dto.alert;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.ORDER;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.SORT;
import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;


@Deprecated
@Getter
public class AlertSearchRequest implements Serializable {

  private final int offset;
  private final int limit;
  private final List<String> priorities;
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final List<String> alertTypes;
  private final List<String> entities;
  private final String alertId;
  private final String sort;
  private final String order;

  @JsonCreator
  public AlertSearchRequest(@JsonProperty("offset") Integer offset,
      @JsonProperty("limit") Integer limit,
      @JsonProperty("priorities") List<String> priorities,
      @JsonProperty("dateFrom") ZonedDateTime dateFrom,
      @JsonProperty("dateTo") ZonedDateTime dateTo,
      @JsonProperty("alertTypes") List<String> alertTypes,
      @JsonProperty("entities") List<String> entities,
      @JsonProperty("alertId") String alertId,
      @JsonProperty("sort") String sort,
      @JsonProperty("order") String order) {
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
