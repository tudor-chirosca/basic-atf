package com.vocalink.crossproduct.ui.aspects;

import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_SEARCH_ENQUIRY;
import static com.vocalink.crossproduct.ui.aspects.OperationType.REQUEST;
import static com.vocalink.crossproduct.ui.aspects.OperationType.RESPONSE;

import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FileEnquiryAspect {

  public static final String RESPONSE_SUCCESS = "OK";
  public static final String RESPONSE_FAILURE = "NOK";
  public static final String X_USER_ID_HEADER = "x-user-id";
  public static final String X_PARTICIPANT_ID_HEADER = "x-participant-id";

  @Value("${app.logging.correlation.mdc}")
  private String mdcKey;

  private final AuditFacade auditFacade;

  @Pointcut("execution(public * com.vocalink.crossproduct.ui.controllers.FilesController.*(..))")
  public void fileEnquiryOperation() {
  }

  @Around(value = "fileEnquiryOperation()")
  Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    final ClientType client = (ClientType) joinPoint.getArgs()[0];
    final String product = (String) joinPoint.getArgs()[1];
    final String content = joinPoint.getArgs()[2].toString();
    final HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[3];

    OccurringEvent event = create(product, client, request, content, FILE_SEARCH_ENQUIRY, REQUEST);

    auditFacade.handleEvent(event);

    try {
      Object obj = joinPoint.proceed();

      event = create(product, client, request, resolveContent((ResponseEntity<?>) obj),
          FILE_SEARCH_ENQUIRY, RESPONSE);

      auditFacade.handleEvent(event);

      return obj;
    } catch (Throwable throwable) {
      event = create(product, client, request, RESPONSE_FAILURE, FILE_SEARCH_ENQUIRY,
          RESPONSE);

      auditFacade.handleEvent(event);

      throw throwable;
    }
  }

  private OccurringEvent create(String product, ClientType client, HttpServletRequest request,
      String content, EventType eventType, OperationType operationType) {

    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String participantId = request.getHeader(X_PARTICIPANT_ID_HEADER);
    final String correlationId = MDC.get(mdcKey);
    final String requestUrl = request.getRequestURI();

    return OccurringEvent.builder()
        .product(product)
        .client(client.name())
        .content(content)
        .eventType(eventType)
        .operationType(operationType)
        .userId(userId)
        .participantId(participantId)
        .requestUrl(requestUrl)
        .correlationId(correlationId)
        .build();
  }

  private String resolveContent(ResponseEntity<?> response) {
    if (response != null && response.getBody() != null) {
      return RESPONSE_SUCCESS;
    }
    return RESPONSE_FAILURE;
  }
}
