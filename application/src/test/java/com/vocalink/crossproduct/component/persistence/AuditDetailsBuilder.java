package com.vocalink.crossproduct.component.persistence;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository;
import com.vocalink.crossproduct.domain.audit.AuditSearchRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsJpa;
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AuditDetailsBuilder implements AuditDetailsRepository {

    private final AuditDetailsRepositoryJpa auditDetailsRepository;

    public void saveAuditDetails() {

        AuditDetailsJpa details = AuditDetailsJpa.builder()
                                 .id(UUID.randomUUID())
                                 .activityName("activityString")
                                 .scheme("P27-SEK")
                                 .approvalRequestId("n/a")
                                 .correlationId("corId")
                                 .channel("BPS")
                                 .username("test-user-001")
                                 .requestOrResponseEnum("blah")
                                 .requestUrl("reqUrl")
                                 .contents("contentJson")
                                 .firstName("John")
                                 .lastName("Snow")
                                 .participantId("HANDSESS")
                                 .build();

        auditDetailsRepository.save(details);
    }

    @Override
    public String getProduct() {
        return null;
    }

    @Override
    public List<AuditDetails> getAuditDetailsByParticipantId(String participantId) {
        return null;
    }

    @Override
    public AuditDetails getAuditDetailsById(String id) {
        return null;
    }

    @Override
    public Page<AuditDetails> getAuditDetailsByParameters(AuditSearchRequest parameters) {
        return null;
    }

    @Override
    public void logOperation(Event event, UserDetails userDetails) {

    }

    @Override
    public AuditDetails getAuditDetailsByUsername(String name) {
        return null;
    }

    @Override
    public List<AuditDetails> getAuditDetailsByParticipantIdAndGroupByUser(String id) {
        return null;
    }

    @Override
    public List<AuditDetails> getAuditDetailsByCorrelationId(String id) {
        return null;
    }

    @Override
    public Optional<UserDetails> getUserDetailsById(String participantId, String userId) {
        return Optional.empty();
    }
}
