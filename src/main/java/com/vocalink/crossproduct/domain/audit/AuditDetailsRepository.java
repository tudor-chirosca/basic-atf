package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface AuditDetailsRepository extends CrossproductRepository {

  List<AuditDetails> getAuditDetailsByParticipantId(String participantId);

  AuditDetails getAuditDetailsById(String id);

  List<AuditDetails> getAuditDetailsByParameters(AuditSearchRequest parameters);

  void logOperation(Event event, UserDetails userDetails);

  AuditDetails getAuditDetailsByUsername(String name);

  List<AuditDetails> getGetUserReferencesByParticipantId(String id);
}
