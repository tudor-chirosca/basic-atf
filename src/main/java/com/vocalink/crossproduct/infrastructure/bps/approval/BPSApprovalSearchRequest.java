package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSApprovalSearchRequest {

  private final List<BPSSortingQuery> sortingOrder;
}
