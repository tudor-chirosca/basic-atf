package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface SettlementsRepository extends CrossproductRepository {

  ParticipantSettlement findBy(BPSInstructionEnquirySearchCriteria request);

  Page<ParticipantSettlement> findBy(BPSSettlementEnquirySearchCriteria request);
}
