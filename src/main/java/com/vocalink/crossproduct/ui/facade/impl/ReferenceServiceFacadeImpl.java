package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.repository.ParticipantRepository;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferenceServiceFacade;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReferenceServiceFacadeImpl implements ReferenceServiceFacade {

  private final ParticipantRepository participantRepository;

  @Override
  public List<ParticipantReferenceDto> getParticipants(String context) {
    log.info("Fetching all reference participants {} ... ", context);
    return participantRepository.findAll(context).stream()
        .map(p -> new ParticipantReferenceDto(p.getBic(), p.getName()))
        .sorted(Comparator.comparing(ParticipantReferenceDto::getName))
        .collect(Collectors.toList());
  }
}
