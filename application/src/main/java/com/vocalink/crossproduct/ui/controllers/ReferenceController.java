package com.vocalink.crossproduct.ui.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.ui.controllers.api.ReferenceApi;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.domain.reference.DestinationType;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.facade.api.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReferenceController implements ReferenceApi {

  private final ReferencesServiceFacade referencesServiceFacade;

  @GetMapping(value = "/reference/participants", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ParticipantReferenceDto>> getReferenceParticipants(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @RequestParam(value="destination", required = false) String destination) {

    if(EnumUtils.getEnum(DestinationType.class, destination) == null && destination != null){
      throw new ClientRequestException(BAD_REQUEST,
          "Invalid destination value: " + destination);
    }

    final List<ParticipantReferenceDto> participantReferenceDto = referencesServiceFacade
        .getParticipantReferences(context.toUpperCase(), clientType, destination);

    return ResponseEntity.ok().body(participantReferenceDto);
  }

  @GetMapping(value = "/reference/enquiry-statuses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReasonCodeReferenceDto>> findFileReferencesByType(
      final @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      final @RequestParam String type) {

    final List<ReasonCodeReferenceDto> files = referencesServiceFacade
        .getReasonCodeReferences(context.toUpperCase(), clientType, type);

    return ResponseEntity.ok().body(files);
  }

  @GetMapping(value = "/reference/messages", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MessageDirectionReferenceDto>> getMessageDirectionReferences(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context) {

    final List<MessageDirectionReferenceDto> messageDirectionReferenceDto = referencesServiceFacade
        .getMessageDirectionReferences(context, clientType);

    return ResponseEntity.ok().body(messageDirectionReferenceDto);
  }

  @GetMapping(value = "reference/cycles", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DayCycleDto>> getDayCyclesByDate(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @RequestParam(value = "day") String date,
      final @RequestParam(value = "settled", required = false) boolean settled) {

    final List<DayCycleDto> cycleDto = referencesServiceFacade
        .getDayCyclesByDate(context, clientType, ZonedDateTime.parse(date), settled);

    return ResponseEntity.ok().body(cycleDto);
  }
}
