package com.vocalink.crossproduct.infrastructure.jpa.audit;

import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivity;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_audit_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuditDetails {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(updatable = false, nullable = false)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "activity_id", nullable = false, updatable = false)
  private UserActivity activityId;
  @Column(updatable = false, insertable = false)
  private ZonedDateTime timestamp;
  private String customer;
  private String ipsSuiteApplicationName;
  @Column(nullable = false)
  private String channel;
  private String ipAddress;
  @Column(nullable = false)
  private String username;
  private String employerOrRepresentation;
  private String userRoleList;
  @Column(nullable = false)
  private String userActivityString;
  @Column(nullable = false)
  private String correlationId;
  @Column(updatable = false, insertable = false, unique = true)
  private Long serviceId;
  @Column(nullable = false)
  private String approvalRequestId;
  @Column(nullable = false)
  private String requestOrResponseEnum;
  @Column(nullable = false)
  private String requestUrl;
  @Column(nullable = false)
  private String contents;
  @Column(nullable = false)
  private String userId;
  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;
  @Column(nullable = false)
  private String participantId;

  @PrePersist
  public void prePersist() {
    this.timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
  }
}
