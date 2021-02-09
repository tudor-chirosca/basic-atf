package com.vocalink.crossproduct.ui.facade;


import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest;
import com.vocalink.crossproduct.ui.facade.api.RoutingRecordFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoutingRecordFacadeImpl implements RoutingRecordFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public PageDto<RoutingRecordDto> getPaginated(String product, ClientType clientType,
      RoutingRecordRequest requestDto, String bic) {
    log.info("Fetching routing records for: {}, from: {}", bic, product);

    final RoutingRecordCriteria request = MAPPER.toEntity(requestDto, bic);

    final Page<RoutingRecord> routingRecords = repositoryFactory.getRoutingRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentRoutingRecords(routingRecords);
  }

}
