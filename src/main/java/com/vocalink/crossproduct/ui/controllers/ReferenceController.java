package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.facade.ReferenceServiceFacade;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReferenceController implements ReferenceApi {

  private final ReferenceServiceFacade referenceServiceFacade;

  @GetMapping(value = "/reference/participants", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ParticipantReferenceDto>> getReferenceParticipants(
      HttpServletRequest httpServletRequest) {
    String contextHeader = httpServletRequest.getHeader("context");

    List<ParticipantReferenceDto> participantReferenceDto = referenceServiceFacade
        .getParticipants(contextHeader.toUpperCase());

    return ResponseEntity.ok().body(participantReferenceDto);
  }
}
