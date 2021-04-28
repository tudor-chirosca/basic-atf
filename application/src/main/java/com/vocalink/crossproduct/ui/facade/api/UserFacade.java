package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface UserFacade {

  CurrentUserInfoDto getCurrentUserInfo(String product, ClientType clientType, String userId,
      String participantId, List<String> roles);
}
