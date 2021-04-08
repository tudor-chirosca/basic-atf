package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface UserFacade {

  CurrentUserInfoDto getCurrentUserInfo(String product, ClientType clientType, String userId,
      String participantId, String role);
}
