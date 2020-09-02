package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;

public interface InputOutputFacade {
  IODashboardDto getInputOutputDashboard(String context, ClientType clientType, LocalDate date);
}
