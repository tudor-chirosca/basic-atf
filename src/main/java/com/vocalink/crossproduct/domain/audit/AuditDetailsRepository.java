package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import java.util.List;

public interface AuditDetailsRepository extends CrossproductRepository {

  List<AuditDetails> getAuditDetailsById(String participantId);

  List<AuditDetails> getAuditDetailsByParameters(AuditRequestParams parameters);
}
