package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface SettlementsRepository extends CrossproductRepository {

  ParticipantSettlement findBy(InstructionEnquirySearchCriteria criteria);

  Page<ParticipantSettlement> findPaginated(SettlementEnquirySearchCriteria criteria);

  List<SettlementSchedule> findSchedules();
}
