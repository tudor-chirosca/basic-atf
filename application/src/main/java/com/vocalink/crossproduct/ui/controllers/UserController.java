package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.controllers.api.UserApi;
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.facade.api.UserFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

  public static final String X_USER_ID_HEADER = "x-user-id";
  public static final String X_PARTICIPANT_ID_HEADER = "x-participant-id";
  public static final String X_ROLES_HEADER = "x-roles";
  public static final String CLIENT_TYPE_HEADER = "client-type";

  private final UserFacade userFacade;

  @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CurrentUserInfoDto> getCurrentUserInfo(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader String context,
      final HttpServletRequest request) {
    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String participantId = request.getHeader(X_PARTICIPANT_ID_HEADER);
    final List<String> roles = Arrays.asList(request.getHeader(X_ROLES_HEADER).split("\\s*,\\s*"));

    CurrentUserInfoDto currentUserInfoDto = userFacade
        .getCurrentUserInfo(context, clientType, userId, participantId, roles);

    return ResponseEntity.ok(currentUserInfoDto);
  }
}
