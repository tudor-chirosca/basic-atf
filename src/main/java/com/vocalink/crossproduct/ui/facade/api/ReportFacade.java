package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import org.springframework.core.io.Resource;

public interface ReportFacade {

  PageDto<ReportDto> getPaginated(String product, ClientType clientType,
      ReportsSearchRequest parameters);

  Resource getReport(String product, ClientType clientType, String reportId);

}
