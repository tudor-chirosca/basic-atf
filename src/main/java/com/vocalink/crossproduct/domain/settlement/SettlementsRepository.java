package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface SettlementsRepository extends CrossproductRepository {

  Page<SettlementDetails> findDetails(SettlementDetailsSearchCriteria criteria);

  Page<ParticipantSettlement> findPaginated(SettlementEnquirySearchCriteria criteria);

  SettlementSchedule findSchedule();
}
