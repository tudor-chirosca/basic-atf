package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface AuditDetailsRepository extends CrossproductRepository {

  List<AuditDetails> getAuditDetailsByParticipantId(String participantId);

  AuditDetails getAuditDetailsById(String id);

  Page<AuditDetails> getAuditDetailsByParameters(AuditSearchRequest parameters);

  void logOperation(Event event, UserDetails userDetails);

  AuditDetails getAuditDetailsByUsername(String name);

  List<AuditDetails> getAuditDetailsByParticipantIdAndGroupByUser(String id);

  List<AuditDetails> getAuditDetailsByCorrelationId(String id);
}
