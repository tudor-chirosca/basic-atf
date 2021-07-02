package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.USER_SESSION_END;
import static com.vocalink.crossproduct.ui.aspects.EventType.USER_SESSION_START;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.UserApi;
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.facade.api.UserFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

  public static final String X_USER_ID_HEADER = "x-user-id";
  public static final String X_PARTICIPANT_ID_HEADER = "x-participant-id";
  public static final String X_ROLES_HEADER = "x-roles";
  public static final String CLIENT_TYPE_HEADER = "client-type";

  private final UserFacade userFacade;

  @Auditable(type = USER_SESSION_START, params = @Positions(clientType = 0, context = 1, request = 2))
  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CurrentUserInfoDto> getCurrentUserInfo(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader String context,
      final HttpServletRequest request) {
    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String participantId = request.getHeader(X_PARTICIPANT_ID_HEADER);
    final List<String> roles = Arrays.asList(request.getHeader(X_ROLES_HEADER).split("\\s*,\\s*"));

    log.info("Session start for user: {}", userId);

    CurrentUserInfoDto currentUserInfoDto = userFacade
        .getCurrentUserInfo(context, clientType, userId, participantId, roles);

    return ResponseEntity.ok(currentUserInfoDto);
  }


  @Auditable(type = USER_SESSION_END, params = @Positions(clientType = 0, context = 1, request = 2))
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @GetMapping(value = "/logout")
  public void logoutUser(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader String context,
      final HttpServletRequest request) {
    final String userId = request.getHeader(X_USER_ID_HEADER);
    log.info("Session end for user: {}", userId);
  }
}
