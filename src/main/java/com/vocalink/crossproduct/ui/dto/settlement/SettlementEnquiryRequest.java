package com.vocalink.crossproduct.ui.dto.settlement;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SettlementEnquiryRequest {

  @Setter
  private int offset = parseInt(getDefault(OFFSET));
  @Setter
  private int limit = parseInt(getDefault(LIMIT));
  @Setter
  private List<String> sort;
  @ValidFromDate
  private LocalDate dateFrom = LocalDate.now().minusDays(parseLong(getDefault(DAYS_LIMIT)));
  private LocalDate dateTo;
  private List<String> cycleIds;
  @Setter
  private List<String> participants;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDate.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDate.parse(dateTo);
  }

  public void setCycle_ids(List<String> cycleIds) {
    this.cycleIds = cycleIds;
  }
}
