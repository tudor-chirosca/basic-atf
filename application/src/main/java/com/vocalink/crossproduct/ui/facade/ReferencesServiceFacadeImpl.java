package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.ui.util.DateTimeAdjustingUtils.adjustWithZoneId;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.reference.DestinationType;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.OutputFlowReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference.Validation;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.OutputFlowReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.facade.api.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReferencesServiceFacadeImpl implements ReferencesServiceFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;
  private final Environment environment;
  private final String zoneId;

  @Autowired
  public ReferencesServiceFacadeImpl(
      PresenterFactory presenterFactory,
      RepositoryFactory repositoryFactory,
      Environment environment,
      @Value("${app.ui.config.default.timeZone}") String zoneId) {
    this.presenterFactory = presenterFactory;
    this.repositoryFactory = repositoryFactory;
    this.environment = environment;
    this.zoneId = zoneId;
  }

  @Override
  public List<ParticipantReferenceDto> getParticipantReferences(String product,
      ClientType clientType, String destination) {
    log.info("Fetching participant references for: {}, from: {}, with: {} destination", clientType,
        product, destination);

    final String participantTypeCsv = destination == null ? null :
        environment.getProperty(DestinationType.valueOf(destination).getPropertyKey());

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll().getItems();

    return presenterFactory.getPresenter(clientType)
        .presentParticipantReferences(participants, getParticipantTypes(participantTypeCsv));
  }

  private List<ParticipantType> getParticipantTypes(String participantTypeCsv) {
    if(participantTypeCsv != null) {
      return Arrays
          .stream(participantTypeCsv.split(","))
          .map(ParticipantType::valueOf)
          .collect(toList());
    }
    return Collections.emptyList();
  }

  @Override
  public List<MessageDirectionReferenceDto> getMessageDirectionReferences(String product,
      ClientType clientType) {
    log.info("Fetching message direction references for: {} from: {}", clientType, product);

    final List<MessageDirectionReference> messageDirectionReferences = repositoryFactory
        .getReferencesRepository(product).findMessageDirectionReferences();

    return presenterFactory.getPresenter(clientType)
        .presentMessageDirectionReferences(messageDirectionReferences);
  }

  @Override
  public List<ReasonCodeReferenceDto> getReasonCodeReferences(String product, ClientType clientType,
      String enquiryType) {
    log.info("Fetching file references with type: {} for: {} from: {}", enquiryType, clientType, product);

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
    log.info("Fetching cycles by date: {} for: {} from: {}", date, clientType, product);

    final ZonedDateTime valueDateUTC = adjustWithZoneId(date, zoneId);

    final List<DayCycle> cycles = repositoryFactory.getCycleRepository(product)
        .findByDate(valueDateUTC)
        .stream()
        .filter(settled ? c -> !c.getStatus().equals(CycleStatus.OPEN) : c -> true)
        .collect(toList());

    return presenterFactory.getPresenter(clientType).presentCycleDateReferences(cycles);
  }

  @Override
  public List<OutputFlowReferenceDto> getOutputFlowReferences(String product,
      ClientType clientType) {

    final List<OutputFlowReference> outputFlowReferences = repositoryFactory
        .getReferencesRepository(product)
        .findOutputFlowReferences();

    return presenterFactory.getPresenter(clientType)
        .presentOutputFlowReferences(outputFlowReferences);
  }
}
