package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferencesServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    List<ParticipantReferenceDto> participantReferenceDto = referencesServiceFacade
        .getParticipantReferences(context.toUpperCase(), clientType);

    return ResponseEntity.ok().body(participantReferenceDto);
  }

  @Deprecated
  @GetMapping(value = "/reference/file-statuses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FileStatusesDto>> getFileReferences(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    List<FileStatusesDto> files = referencesServiceFacade
        .getFileReferences(context.toUpperCase(), clientType);

    return ResponseEntity.ok().body(files);
  }

  @GetMapping(value = "/reference/enquiry-statuses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FileStatusesTypeDto>> findFileReferencesByType(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @RequestParam String type) {

    List<FileStatusesTypeDto> files = referencesServiceFacade
        .getFileReferences(context.toUpperCase(), clientType, type);

    return ResponseEntity.ok().body(files);
  }

  @GetMapping(value = "/reference/messages", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MessageDirectionReferenceDto>> getMessageDirectionReferences(
      @RequestHeader("client-type") ClientType clientType,
      @RequestHeader String context) {

    List<MessageDirectionReferenceDto> messageDirectionReferenceDto = referencesServiceFacade
        .getMessageDirectionReferences(context, clientType);

    return ResponseEntity.ok().body(messageDirectionReferenceDto);
  }

  @GetMapping(value = "reference/cycles", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CycleDto>> getCycleByDate(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @RequestParam(value = "day") String date) {

    List<CycleDto> cycleDto = referencesServiceFacade.getCyclesByDate(context, clientType, LocalDate.parse(date));

    return ResponseEntity.ok().body(cycleDto);
  }
}
