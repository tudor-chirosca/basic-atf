package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "user_audit_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuditDetailsJpa implements Serializable {

  private static final String serialVersionUID = "275a3d8b-5a8a-4bac-9097-e7f9d495374f";

  @Id
  @Type(type="uuid-char")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "activity_id", foreignKey = @ForeignKey(name = "AI_FK"), nullable = false, updatable = false)
  private UserActivityJpa activityId;
  @Column(name = "timestamp", updatable = false)
  private ZonedDateTime timestamp;
  @Column(name = "customer")
  private String customer;
  @Column(name = "ips_suite_application_name")
  private String ipsSuiteApplicationName;
  @Column(name = "channel", nullable = false)
  private String channel;
  @Column(name = "ip_address")
  private String ipAddress;
  @Column(name = "username", nullable = false)
  private String username;
  @Column(name = "employer_or_representation")
  private String employerOrRepresentation;
  @Column(name = "user_role_list")
  private String userRoleList;
  @Column(name = "user_activity_string", nullable = false)
  private String userActivityString;
  @Column(name = "correlation_id", nullable = false)
  private String correlationId;
  @Column(name = "service_id", updatable = false, insertable = false, unique = true)
  private Long serviceId;
  @Column(name = "approval_request_id", nullable = false)
  private String approvalRequestId;
  @Column(name = "request_or_response_enum", nullable = false)
  private String requestOrResponseEnum;
  @Column(name = "request_url", nullable = false)
  private String requestUrl;
  @Column(name = "contents", nullable = false)
  private String contents;
  @Column(name = "first_name", nullable = false)
  private String firstName;
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "participant_id", nullable = false)
  private String participantId;

  @PrePersist
  public void prePersist() {
    this.timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
  }
}
