package com.vocalink.crossproduct.ui.dto.io;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantIODataDto {
  private ParticipantDto participant;
  private IODataDto files;
  private IODataDto batches;
  private IODataDto transactions;
}
