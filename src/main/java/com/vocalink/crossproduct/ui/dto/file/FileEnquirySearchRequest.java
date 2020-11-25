package com.vocalink.crossproduct.ui.dto.file;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FileEnquirySearchRequest {

  @Setter
  private int offset = parseInt(getDefault(OFFSET));
  @Setter
  private int limit = parseInt(getDefault(LIMIT));
  @Setter
  private List<String> sort;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private List<String> cycleIds;
  private String messageDirection;
  private String messageType;
  private String sendingBic;
  private String receivingBic;
  @Setter
  private String status;
  private String reasonCode;
  @Setter
  private String id;

  public void setDate_from(String dateFrom) {
    this.dateFrom = LocalDate.parse(dateFrom);
  }

  public void setDate_to(String dateTo) {
    this.dateTo = LocalDate.parse(dateTo);
  }

  public void setCycle_ids(List<String> cycleIds) {
    this.cycleIds = cycleIds;
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
