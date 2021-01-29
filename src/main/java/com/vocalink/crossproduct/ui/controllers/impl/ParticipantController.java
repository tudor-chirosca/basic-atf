package com.vocalink.crossproduct.ui.controllers.impl;

import com.vocalink.crossproduct.ui.controllers.ParticipantApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.facade.ParticipantFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ParticipantController implements ParticipantApi {

  private final ParticipantFacade participantFacade;

  @GetMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ManagedParticipantDto>> getParticipants(
      @RequestHeader("client-type") ClientType clientType,
      @RequestHeader String context,
      ManagedParticipantsSearchRequest request) {
    PageDto<ManagedParticipantDto> participantsDto = participantFacade
        .getPaginated(context, clientType, request);
    return ResponseEntity.ok().body(participantsDto);
  }
}
