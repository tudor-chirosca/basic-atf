package com.vocalink.crossproduct.ui.facade;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference.Validation;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.api.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferencesServiceFacadeImpl implements ReferencesServiceFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public List<ParticipantReferenceDto> getParticipantReferences(String product,
      ClientType clientType) {
    log.info("Fetching participant references from: {}", product);

    final List<Participant> participants = repositoryFactory
        .getParticipantRepository(product)
        .findAll()
        .getItems();

    return presenterFactory.getPresenter(clientType)
        .presentParticipantReferences(participants);
  }

  @Override
  public List<MessageDirectionReferenceDto> getMessageDirectionReferences(String product,
      ClientType clientType) {
    log.info("Fetching message direction references from: {}", product);

    final List<MessageDirectionReference> messageDirectionReferences = repositoryFactory
        .getReferencesRepository(product).findMessageDirectionReferences();

    return presenterFactory.getPresenter(clientType)
        .presentMessageDirectionReferences(messageDirectionReferences);
  }

  @Override
  public List<ReasonCodeReferenceDto> getReasonCodeReferences(String product, ClientType clientType,
      String enquiryType) {
    log.info("Fetching file references with type: {} from: {}", enquiryType, product);

    final Validation validation = repositoryFactory
        .getReferencesRepository(product)
        .findReasonCodeReference()
        .getValidations()
        .stream()
        .filter(v -> Objects.equals(v.getValidationLevel(), enquiryType))
        .findFirst()
        .orElseThrow(() -> new NonConsistentDataException(
            "No reason codes found for type: " + enquiryType)
        );

    final List<String> statuses = repositoryFactory.getReferencesRepository(product)
        .findStatuses(enquiryType);

    return presenterFactory.getPresenter(clientType)
        .presentReasonCodeReferences(validation, statuses);
  }

  @Override
  public List<DayCycleDto> getDayCyclesByDate(String product, ClientType clientType,
      ZonedDateTime date, boolean settled) {
    log.info("Fetching cycles by date from: {}", product);

    List<DayCycle> cycles = repositoryFactory.getCycleRepository(product)
        .findByDate(date)
        .stream()
        .filter(settled ? c -> !c.getStatus().equals(CycleStatus.OPEN) : c -> true)
        .collect(toList());

    return presenterFactory.getPresenter(clientType).presentCycleDateReferences(cycles);
  }
}
