package com.vocalink.crossproduct.ui.dto.settlement;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SettlementEnquiryRequest {

  @Setter
  private int offset = parseInt(getDefault(OFFSET));
  @Setter
  @ValidLimit
  private int limit = parseInt(getDefault(LIMIT));
  @Setter
  private List<String> sort;
  @ValidFromDate
  private ZonedDateTime dateFrom;
  private ZonedDateTime dateTo;
  private String cycleId;
  @Setter
  private List<String> participants;

  public void setDate_from(String dateFrom) {
    this.dateFrom = ZonedDateTime.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = ZonedDateTime.parse(dateTo);
  }

  public void setCycle_id(String cycleId) {
    this.cycleId = cycleId;
  }
}
