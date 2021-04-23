package com.vocalink.crossproduct.ui.dto.file;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ui.validations.ValidDirection;
import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidLimit;
import com.vocalink.crossproduct.ui.validations.ValidRegexSearch;
import com.vocalink.crossproduct.ui.validations.ValidSort;
import com.vocalink.crossproduct.ui.validations.ValidStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ValidStatus(status = "status", reasonCode = "reasonCode", statuses = {"NAK"})
@ValidSort(sort = "sort", sortingKeys =
    {"name", "createdAt", "senderBic", "messageType", "nrOfBatches", "status"})
@ToString
public class FileEnquirySearchRequest {

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
  private String participantBic;
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

  public void setCycle_ids(List<String> cycleId) {
    this.cycleId = cycleId.stream().findFirst().orElse(null);
  }

  public void setMsg_direction(String messageDirection) {
    this.messageDirection = messageDirection;
  }

  public void setMsg_type(String messageType) {
    this.messageType = messageType;
  }

  public void setParticipant_bic(String participantBic) {
    this.participantBic = participantBic;
  }

  public void setReason_code(String reasonCode) {
    this.reasonCode = reasonCode;
  }
}
