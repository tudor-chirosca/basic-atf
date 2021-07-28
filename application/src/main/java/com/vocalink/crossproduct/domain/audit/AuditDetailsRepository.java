package com.vocalink.crossproduct.domain.audit;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;
import java.util.Optional;

public interface AuditDetailsRepository extends CrossproductRepository {

  List<AuditDetails> getAuditDetailsByParticipantId(String participantId);

  AuditDetails getAuditDetailsById(String id);

  Page<AuditDetails> getAuditDetailsByParameters(AuditSearchRequest parameters);

  void logOperation(Event event, UserDetails userDetails);

  AuditDetails getAuditDetailsByUsername(String name);

  List<AuditDetails> getAuditDetailsByParticipantIdAndGroupByUser(String id);

  List<AuditDetails> getAuditDetailsByCorrelationId(String id);

  Optional<UserDetails> getUserDetailsById(String participantId, String userId);
}
