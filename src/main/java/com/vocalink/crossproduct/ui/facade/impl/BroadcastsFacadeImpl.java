package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.adapter.EntityMapper;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.facade.BroadcastsFacade;
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
        .getBroadcastsRepositories(product)
        .findPaginated(criteria);

    final int totalResults = pagedBroadcasts.getTotalResults();

    final ParticipantRepository participantRepository = repositoryFactory
        .getParticipantRepository(product);

    final List<BroadcastDto> broadcastDtos = pagedBroadcasts.getItems()
        .stream()
        .map(b -> DTOMapper.MAPPER.toDto(b, participantRepository))
        .collect(toList());

    return presenterFactory
        .getPresenter(clientType)
        .presentBroadcasts(totalResults, broadcastDtos);
  }
}
