package com.vocalink.crossproduct.ui.dto.alert;

import com.vocalink.crossproduct.domain.alert.SortOrder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertFilterRequest implements Serializable {

  private Integer offset;
  private Integer limit;
  private List<String> priorities;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private List<String> alertTypes;
  private List<String> entities;
  private String alertId;
  private String sort;
  private SortOrder order;
}
