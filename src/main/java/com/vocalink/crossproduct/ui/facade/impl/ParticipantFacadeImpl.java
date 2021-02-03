package com.vocalink.crossproduct.ui.facade.impl;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.facade.ParticipantFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantFacadeImpl implements ParticipantFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public PageDto<ManagedParticipantDto> getPaginated(String product, ClientType clientType,
      ManagedParticipantsSearchRequest requestDto) {

    final ManagedParticipantsSearchCriteria request = MAPPER.toEntity(requestDto);

    final Page<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findPaginated(request);

    final Page<Participant> managedParticipants = new Page<>(participants.getTotalResults(),
        participants.getItems().stream()
            .peek(p -> {
              if (p.getParticipantType().equals(ParticipantType.DIRECT_FUNDING) ||
                  p.getParticipantType().equals(ParticipantType.FUNDING)) {
                List<Participant> fundedParticipants = repositoryFactory.getParticipantRepository(product)
                    .findByConnectingPartyAndType(p.getId(), ParticipantType.FUNDED.getDescription());
                p.setFundedParticipants(fundedParticipants);
                p.setFundedParticipantsCount(p.getFundedParticipants().size());
                p.getFundedParticipants().sort(comparing(Participant::getName));
              }
            }).collect(toList()));

    return presenterFactory.getPresenter(clientType).presentManagedParticipants(managedParticipants);
  }
}
