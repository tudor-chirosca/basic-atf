package com.vocalink.crossproduct.ui.facade;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.api.BroadcastsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BroadcastsFacadeImpl implements BroadcastsFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public PageDto<BroadcastDto> getPaginated(String product, ClientType clientType,
      BroadcastsSearchParameters parameters) {
    log.info("Fetching broadcasts from: {}", product);

    BroadcastsSearchCriteria criteria = EntityMapper.MAPPER.toEntity(parameters);

    final Page<Broadcast> pagedBroadcasts = repositoryFactory
        .getBroadcastsRepository(product)
        .findPaginated(criteria);

    final ParticipantRepository participantRepository = repositoryFactory
        .getParticipantRepository(product);

    final List<BroadcastDto> broadcasts = pagedBroadcasts.getItems().stream()
        .map(b -> {
          final List<ParticipantReferenceDto> references = b.getRecipients().stream()
              .map(participantRepository::findById)
              .map(DTOMapper.MAPPER::toReferenceDto)
              .collect(toList());
          final BroadcastDto broadcastDto = DTOMapper.MAPPER.toDto(b);
          broadcastDto.setRecipients(references);

          return broadcastDto;
        })
        .collect(toList());

    return presenterFactory
        .getPresenter(clientType)
        .presentBroadcasts(pagedBroadcasts.getTotalResults(), broadcasts);
  }

  @Override
  public BroadcastDto create(String product, ClientType clientType, String message, List<String> recipients) {

    final Broadcast broadcast = repositoryFactory.getBroadcastsRepository(product)
        .create(message, recipients);

    final ParticipantRepository participantRepository = repositoryFactory
        .getParticipantRepository(product);

    final List<Participant> participants = broadcast.getRecipients().stream()
        .map(participantRepository::findById)
        .collect(toList());

    return presenterFactory.getPresenter(clientType).presentBroadcast(broadcast, participants);
  }
}
