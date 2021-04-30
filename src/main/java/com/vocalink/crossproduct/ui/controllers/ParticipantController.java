package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_PARTICIPANT_MNG_DETAILS;
import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_PARTICIPANT_MNG_LIST;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.ParticipantApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ParticipantFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParticipantController implements ParticipantApi {

  private final ParticipantFacade participantFacade;

  @Auditable(type = VIEW_PARTICIPANT_MNG_LIST, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ManagedParticipantDto>> getParticipants(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      final ManagedParticipantsSearchRequest request,
      final HttpServletRequest httpServletRequest) {

    final PageDto<ManagedParticipantDto> participantsDto = participantFacade
        .getPaginated(context, clientType, request);

    return ResponseEntity.ok().body(participantsDto);
  }

  @Auditable(type = VIEW_PARTICIPANT_MNG_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/participants/{bic}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ManagedParticipantDetailsDto> getManagedParticipantDetails(
      @RequestHeader("client-type") ClientType clientType,
      @RequestHeader String context,
      @PathVariable String bic,
      final HttpServletRequest httpServletRequest) {

    final ManagedParticipantDetailsDto managedParticipantDetailsDto = participantFacade
        .getById(context, clientType, bic);

    return ResponseEntity.ok().body(managedParticipantDetailsDto);
  }
}
