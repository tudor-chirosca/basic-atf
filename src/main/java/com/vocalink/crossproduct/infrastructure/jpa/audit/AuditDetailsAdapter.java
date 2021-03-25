package com.vocalink.crossproduct.infrastructure.jpa.audit;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuditDetailsAdapter implements AuditDetailsRepository {

  private final AuditDetailsRepositoryJpa auditDetailsRepository;

  @Override
  public List<AuditDetails> getAuditDetailsById(String participantId) {
    return auditDetailsRepository.findAllByParticipantId(participantId)
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
