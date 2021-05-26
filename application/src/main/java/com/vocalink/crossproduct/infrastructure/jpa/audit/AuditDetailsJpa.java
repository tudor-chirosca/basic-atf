package com.vocalink.crossproduct.infrastructure.jpa.audit;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Entity
@Table(name = "user_audit_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuditDetailsJpa implements Serializable {

  private static final String serialVersionUID = "275a3d8b-5a8a-4bac-9097-e7f9d495374f";
  private static final String DEFAULT_SORT_PARAMETER = "timestamp";
  private static final String ASCENDING_SIGN = "+";
  private static final String DESCENDING_SING = "-";

  @Getter(AccessLevel.PRIVATE)
  private static final Map<String, List<String>> SORT_BINDINGS = new HashMap<>();

  static {
    SORT_BINDINGS.put("createdAt", Collections.singletonList(DEFAULT_SORT_PARAMETER));
    SORT_BINDINGS.put("serviceId", Collections.singletonList("serviceId"));
    SORT_BINDINGS.put("eventType", Collections.singletonList("userActivityString"));
    SORT_BINDINGS.put("user", Arrays.asList("firstName", "lastName"));
    SORT_BINDINGS.put("responseContent", Collections.singletonList("responseContent"));
  }

  @Id
  @Type(type = "uuid-char")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "activity_name", nullable = false, updatable = false)
  private String activityName;
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

  public static Sort getSortBy(List<String> sort) {
    if (sort == null || sort.isEmpty()) {
      return Sort.by(DEFAULT_SORT_PARAMETER).descending();
    }

    final List<Order> orders = new ArrayList<>();

    sort.forEach(sortParameter -> {
      final String field = (sortParameter.startsWith(ASCENDING_SIGN)
              || sortParameter.startsWith(DESCENDING_SING)) ? sortParameter.substring(1) : sortParameter;
      if (!SORT_BINDINGS.containsKey(field)) {
        return;
      }
      final String direction = (sortParameter.startsWith(ASCENDING_SIGN)
              || sortParameter.startsWith(DESCENDING_SING)) ? sortParameter.substring(0, 1) : EMPTY;
      final List<String> entityFields = SORT_BINDINGS.getOrDefault(field, Collections.emptyList());
      if (DESCENDING_SING.equals(direction)) {
        entityFields.forEach(s -> orders.add(Order.desc(s)));
      } else {
        entityFields.forEach(s -> orders.add(Order.asc(s)));
      }
    });

    return Sort.by(orders);
  }
}
