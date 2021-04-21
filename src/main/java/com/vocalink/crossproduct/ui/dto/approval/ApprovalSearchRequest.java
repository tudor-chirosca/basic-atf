package com.vocalink.crossproduct.ui.dto.approval;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import com.vocalink.crossproduct.ui.validations.ValidFromDate;
import com.vocalink.crossproduct.ui.validations.ValidSort;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@ValidSort(sort = "sort", sortingKeys =
    {"participants", "requestType", "jobId", "createdAt", "requestedBy", "status"})
public class ApprovalSearchRequest {

  @Setter
  private int offset = parseInt(getDefault(OFFSET));
  @Setter
  private int limit = parseInt(getDefault(LIMIT));
  private String jobId;
  @ValidFromDate
  private ZonedDateTime fromDate = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(parseLong(getDefault(DAYS_LIMIT)));
  private ZonedDateTime toDate;
  private List<String> participantIds;
  private List<String> requestTypes;
  private List<String> requestedBy;
  @Setter
  private List<String> statuses;
  @Setter
  private List<String> sort;

  public void setFrom_date(String fromDate) {
    this.fromDate = ZonedDateTime.parse(fromDate);
  }

  public void setTo_date(String toDate) {
    this.toDate = ZonedDateTime.parse(toDate);
  }

  public void setJob_id(String jobId) {
    this.jobId = jobId;
  }

  public void setParticipant_ids(List<String> participantIds) {
    this.participantIds = participantIds;
  }

  public void setRequest_types(List<String> requestTypes) {
    this.requestTypes = requestTypes;
  }

  public void setRequested_by(List<String> requestedBy) {
    this.requestedBy = requestedBy;
  }
}
