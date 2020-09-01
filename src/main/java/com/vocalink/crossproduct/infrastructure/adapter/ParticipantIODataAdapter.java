package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.IOData;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.ParticipantIODataRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.io.ParticipantIODataDto;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ParticipantIODataAdapter implements ParticipantIODataRepository {
  private final ClientFactory clientFactory;

  @Override
  public List<ParticipantIOData> findAll(String context) {
    ParticipantClient participantClient = clientFactory.getParticipantClient(context);
    return participantClient.findParticipantIOData()
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
