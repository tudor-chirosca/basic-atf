package com.vocalink.crossproduct.ui.dto.settlement;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ParticipantSettlementRequest {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> sort;
  private String cycleId;
  private String participantId;

}
