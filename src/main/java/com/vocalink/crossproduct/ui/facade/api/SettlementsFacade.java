package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementsFacade {

  ParticipantSettlementDetailsDto getSettlementDetails(String context, ClientType clientType,
      ParticipantSettlementRequest request);

  PageDto<ParticipantSettlementCycleDto> getSettlements(String context, ClientType clientType,
      SettlementEnquiryRequest request);

  LatestSettlementCyclesDto getLatestCycles(String context, ClientType clientType);

  SettlementScheduleDto getSettlementsSchedule(String product,
      ClientType clientType);
}
