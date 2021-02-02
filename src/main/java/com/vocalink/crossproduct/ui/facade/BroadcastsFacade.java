package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface BroadcastsFacade {

  PageDto<BroadcastDto> getPaginated(String product, ClientType clientType,
      BroadcastsSearchParameters parameters);

}
