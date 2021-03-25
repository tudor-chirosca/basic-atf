package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface AuditDetailsRepository extends CrossproductRepository {

  List<AuditDetails> getAuditDetailsById(String participantId);

}
