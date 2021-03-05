package com.vocalink.crossproduct.ui.dto.batch;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ui.validations.NotEqual;
import com.vocalink.crossproduct.ui.validations.ValidDirection;
import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import com.vocalink.crossproduct.ui.validations.ValidRegexSearch;
import com.vocalink.crossproduct.ui.validations.ValidStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@NotEqual(first = "sendingBic", second = "receivingBic", message = "send_bic and recv_bic should not be the same")
@ValidStatus(status = "status", reasonCode = "reasonCode", statuses = {"PRE-RJCT", "POST-RJCT"})
public class BatchEnquirySearchRequest {

  @Setter
  private int offset = parseInt(getDefault(OFFSET));
  @Setter
  @ValidLimit
  private int limit = parseInt(getDefault(LIMIT));
  @Setter
  private List<String> sort;
  @ValidFromDate
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private String cycleId;
  @ValidDirection
  private String messageDirection;
  private String messageType;
  private String sendingBic;
  private String receivingBic;
  @Setter
  private String status;
  private String reasonCode;
  @Setter
  @ValidRegexSearch(regExp = "^(\\*?)[a-zA-Z0-9_.]+(\\*?)$")
  private String id;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDate.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDate.parse(dateTo);
  }

  public void setCycle_ids(List<String> cycleIds) {
    this.cycleId = cycleIds.stream().findFirst().orElse(null);
  }

  public void setMsg_direction(String messageDirection) {
    this.messageDirection = messageDirection;
  }

  public void setMsg_type(String messageType) {
    this.messageType = messageType;
  }

  public void setSend_bic(String sendingBic) {
    this.sendingBic = sendingBic;
  }

  public void setRecv_bic(String receivingBic) {
    this.receivingBic = receivingBic;
  }

  public void setReason_code(String reasonCode) {
    this.reasonCode = reasonCode;
  }
}
