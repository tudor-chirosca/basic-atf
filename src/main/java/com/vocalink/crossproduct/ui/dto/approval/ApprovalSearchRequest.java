package com.vocalink.crossproduct.ui.dto.approval;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ui.validations.ValidSort;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidSort(sort = "sort", sortingKeys =
    {"participantName", "requestType", "jobId", "createdAt", "requestedBy", "status"})
public class ApprovalSearchRequest {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> sort;
}
