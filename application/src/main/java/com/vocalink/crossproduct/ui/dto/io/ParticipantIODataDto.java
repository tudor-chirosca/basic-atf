package com.vocalink.crossproduct.ui.dto.io;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantIODataDto {

  private final ParticipantDto participant;
  private final IODataDto files;
  private final IODataDto batches;
  private final IODataDto transactions;
}
