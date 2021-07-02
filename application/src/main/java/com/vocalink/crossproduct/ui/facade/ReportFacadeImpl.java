package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DOWNLOAD_REPORT_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import com.vocalink.crossproduct.ui.facade.api.ReportFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportFacadeImpl implements ReportFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public PageDto<ReportDto> getPaginated(String product, ClientType clientType,
      ReportsSearchRequest parameters) {
    log.info("Fetching reports for: {} from: {}", clientType, product);

    final ParticipantRepository participantRepository = repositoryFactory
            .getParticipantRepository(product);

    final List<Participant> participants = ofNullable(parameters.getParticipants()).orElse(emptyList()).stream()
            .map(participantRepository::findById)
            .collect(toList());

    final ReportSearchCriteria criteria = MAPPER.toEntity(parameters, participants);

    final Page<Report> pagedReports = repositoryFactory.getReportRepository(product)
        .findPaginated(criteria);

    return presenterFactory.getPresenter(clientType).presentReports(pagedReports);
  }

  @Override
  public void writeReportToOutputStream(String product, ClientType clientType, String reportId, OutputStream outputStream) {
    log.info("Write to output stream report id: {} for: {} from: {}", reportId, clientType, product);
    try {
      serviceFactory.getDownloadService(product)
            .writeResourceToOutputStream(DOWNLOAD_REPORT_PATH, reportId, outputStream);
    } catch (IOException e) {
      throw new UILayerException(e, "Exception thrown while reading input stream.");
    }
  }
}
