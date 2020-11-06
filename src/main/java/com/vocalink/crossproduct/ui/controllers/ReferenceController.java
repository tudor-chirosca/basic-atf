package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferenceServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReferenceController implements ReferenceApi {

  private final ReferenceServiceFacade referenceServiceFacade;

  @GetMapping(value = "/reference/participants", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ParticipantReferenceDto>> getReferenceParticipants(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    List<ParticipantReferenceDto> participantReferenceDto = referenceServiceFacade
        .getParticipants(context.toUpperCase());

    return ResponseEntity.ok().body(participantReferenceDto);
  }

  @GetMapping(value = "/reference/file-statuses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FileStatusesDto>> getFileReferences(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    List<FileStatusesDto> files = referenceServiceFacade
        .getFileReferences(context.toUpperCase(), clientType);

    return ResponseEntity.ok().body(files);
  }

}
