package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.IOData;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.ParticipantIODataRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import com.vocalink.crossproduct.shared.io.ParticipantIODataDto;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ParticipantIODataAdapter implements ParticipantIODataRepository {
  private final ClientFactory clientFactory;

  @Override
  public List<ParticipantIOData> findByTimestamp(String context, LocalDate dateFrom) {
    log.info("Fetching all cycles from context {} ... ", context);
    ParticipantIODataClient participantIODataClient = clientFactory.getParticipantIODataClient(context);
    return participantIODataClient.findByTimestamp(dateFrom)
        .stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

  private ParticipantIOData toEntity(ParticipantIODataDto dto){
    return ParticipantIOData.builder()
        .participantId(dto.getParticipantId())
        .batches(IOData.builder()
            .rejected(dto.getBatches().getRejected())
            .submitted(dto.getBatches().getSubmitted())
            .build()
        )
        .files(IOData.builder()
            .rejected(dto.getFiles().getRejected())
            .submitted(dto.getFiles().getSubmitted()).build()
        )
        .transactions(IOData.builder()
            .rejected(dto.getTransactions().getRejected())
            .submitted(dto.getTransactions().getSubmitted()).build()
        )
        .build();
  }
}
