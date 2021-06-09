package com.vocalink.crossproduct.ui.aspects;

import static com.vocalink.crossproduct.ui.aspects.EventType.AMEND_PARTICIPANT_CONFIG;
import static com.vocalink.crossproduct.ui.aspects.OperationType.REQUEST;
import static com.vocalink.crossproduct.ui.aspects.OperationType.RESPONSE;
import static com.vocalink.crossproduct.ui.aspects.Positions.POSITION_NOT_SET;
import static java.lang.Boolean.valueOf;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.security.InvalidParameterException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Getter
@Component
@RequiredArgsConstructor
public class AuditAspect {

  public static final String EMPTY_CONTENT = "EMPTY";
  public static final String RESPONSE_SUCCESS = "OK";
  public static final String RESPONSE_FAILURE = "NOK";
  public static final String X_USER_ID_HEADER = "x-user-id";
  public static final String X_PARTICIPANT_ID_HEADER = "x-participant-id";
  public static final String X_POLLING_UI_HEADER = "x-polling-ui";

  @Value("${app.logging.correlation.mdc}")
  private String mdcKey;

  private final AuditFacade auditFacade;
  private final ContentUtils contentUtils;
  private final AuditableDetail auditableDetail;

  @Around(value = "@annotation(auditable)")
  Object log(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
    final Optional<HttpServletRequest> request = getHttpRequest(joinPoint, auditable);

    if (isPolling(request)) {
      return joinPoint.proceed();
    }

    final String client = getClientType(joinPoint, auditable);
    final String product = getProduct(joinPoint, auditable);
    final String jsonString = getContent(joinPoint, auditable);

    final EventType eventType = getEventType(joinPoint, auditable);

    final String userId = request.map(r -> r.getHeader(X_USER_ID_HEADER))
        .orElse(EMPTY);
    final String participantId = request.map(r -> r.getHeader(X_PARTICIPANT_ID_HEADER))
        .orElse(EMPTY);
    final String requestUrl = request.map(r -> r.getRequestURL().toString())
        .orElse(EMPTY);
    final String correlationId = MDC.get(getMdcKey());

    OccurringEvent event = OccurringEvent.builder()
        .product(product)
        .client(client)
        .userId(userId)
        .participantId(participantId)
        .requestUrl(requestUrl)
        .correlationId(correlationId)
        .content(jsonString)
        .eventType(eventType)
        .operationType(REQUEST)
        .build();

    getAuditFacade().handleEvent(event);

    try {
      Object obj = joinPoint.proceed();
      event.setApprovalRequestId(getApprovalRequestId(joinPoint, auditable));
      String responseContent = getResponseContent(eventType);
      getAuditFacade().handleEvent(new OccurringEvent(event, responseContent, RESPONSE));
      return obj;
    } catch (Throwable throwable) {
      event.setApprovalRequestId(getApprovalRequestId(joinPoint, auditable));
      getAuditFacade().handleEvent(new OccurringEvent(event, RESPONSE_FAILURE, RESPONSE));

      throw throwable;
    }
  }

  protected String getResponseContent(EventType eventType) {
    if(AMEND_PARTICIPANT_CONFIG.equals(eventType)) {
      return getContentUtils().toJsonString(auditableDetail.getPreviousValues());
    }
    return RESPONSE_SUCCESS;
  }

  private String getApprovalRequestId(ProceedingJoinPoint joinPoint, Auditable auditable) {
    if (auditable.params().requestId() == POSITION_NOT_SET) {
      return auditableDetail.getJobId();
    }
    return joinPoint.getArgs()[auditable.params().requestId()].toString();
  }

  private boolean isPolling(Optional<HttpServletRequest> request) {
    return request.map(r -> valueOf(r.getHeader(X_POLLING_UI_HEADER)))
        .orElse(false);
  }

  protected String getContent(ProceedingJoinPoint joinPoint, Auditable auditable) {
    if (auditable.params().content() == POSITION_NOT_SET) {
      return EMPTY_CONTENT;
    }
    if (!EventType.UNKNOWN.equals(auditable.type())) {
      return getContentUtils().toJsonString(joinPoint.getArgs()[auditable.params().content()]);
    }
    Object content = joinPoint.getArgs()[auditable.params().content()];
    if (content instanceof AuditableRequest) {
      return getContentUtils().toJsonString(((AuditableRequest) content).getAuditableContent());
    }
    throw new InvalidParameterException("Could not resolve request object at content position");
  }

  protected EventType getEventType(ProceedingJoinPoint joinPoint, Auditable auditable) {
    EventType eventType = auditable.type();
    if (!EventType.UNKNOWN.equals(eventType)) {
      return eventType;
    }
    if (auditable.params().content() == POSITION_NOT_SET) {
      throw new IllegalStateException("No Request object at content position");
    }
    Object content = joinPoint.getArgs()[auditable.params().content()];
    if (content instanceof AuditableRequest) {
      return ((AuditableRequest) content).getEventType();
    }
    throw new InvalidParameterException("Could not resolve request object at content position");
  }

  protected Optional<HttpServletRequest> getHttpRequest(ProceedingJoinPoint joinPoint,
      Auditable auditable) {
    if (auditable.params().request() != POSITION_NOT_SET) {
      return Optional.of((HttpServletRequest) joinPoint.getArgs()[auditable.params().request()]);
    }
    return Optional.empty();
  }

  protected String getProduct(ProceedingJoinPoint joinPoint, Auditable auditable) {
    if (auditable.params().context() != POSITION_NOT_SET) {
      return (String) joinPoint.getArgs()[auditable.params().context()];
    }
    return EMPTY;
  }

  protected String getClientType(ProceedingJoinPoint joinPoint, Auditable auditable) {
    if (auditable.params().clientType() != POSITION_NOT_SET) {
      return ((ClientType) joinPoint.getArgs()[auditable.params().clientType()]).name();
    }
    return EMPTY;
  }
}
