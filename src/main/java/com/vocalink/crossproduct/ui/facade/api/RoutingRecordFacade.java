package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface RoutingRecordFacade {

  PageDto<RoutingRecordDto> getPaginated(String product, ClientType clientType,
      RoutingRecordRequest requestDto, String bic);
}
