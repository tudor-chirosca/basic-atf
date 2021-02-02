package com.vocalink.crossproduct.ui.dto.broadcasts;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroadcastsSearchParameters {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> sort;
  private String recipient;
  private String msg;
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
}
