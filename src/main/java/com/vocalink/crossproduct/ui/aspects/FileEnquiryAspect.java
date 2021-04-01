package com.vocalink.crossproduct.ui.aspects;

import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_SEARCH_ENQUIRY;

import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FileEnquiryAspect {

  public static final String X_USER_ID_HEADER = "x-user-id";
  public static final String X_PARTICIPANT_ID_HEADER = "x-participant-id";
  public static final String NOT_AVAILABLE = "n/a";

  private final AuditFacade auditFacade;

  @Pointcut("execution(public * com.vocalink.crossproduct.ui.controllers.FilesController.*(..))")
  public void fileEnquiryOperation() {
  }

  @Before("fileEnquiryOperation()")
  void logBeforeEnquiry(JoinPoint joinPoint) {
    final ClientType client = (ClientType) joinPoint.getArgs()[0];
    final String product = (String) joinPoint.getArgs()[1];
    final String content = joinPoint.getArgs()[2].toString();
    final HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[3];

    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String participantId = request.getHeader(X_PARTICIPANT_ID_HEADER);
    final String requestUrl = request.getRequestURI();

    final OccurringEvent event = create(product, client, userId, participantId, requestUrl, content,
        FILE_SEARCH_ENQUIRY);

    auditFacade.handleEvent(event);
  }

  @AfterReturning(value = "fileEnquiryOperation()", returning = "response")
  void logAfterEnquiry(JoinPoint joinPoint, ResponseEntity<?> response) {
    final ClientType client = (ClientType) joinPoint.getArgs()[0];
    final String product = (String) joinPoint.getArgs()[1];
    final String content = getContent(response);
    final HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[3];

    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String participantId = request.getHeader(X_PARTICIPANT_ID_HEADER);
    final String requestUrl = request.getRequestURI();

    final OccurringEvent event = create(product, client, userId, participantId, requestUrl, content,
        FILE_SEARCH_ENQUIRY);

    auditFacade.handleEvent(event);
  }

  private String getContent(ResponseEntity<?> response) {
    if (response != null && response.getBody() != null) {
      return response.getBody().toString();
    }
    return NOT_AVAILABLE;
  }

  private OccurringEvent create(String product,
      ClientType client, String username, String participantId, String requestUrl,
      String content, EventType eventType) {

    return OccurringEvent.builder()
        .product(product)
        .client(client.name())
        .userId(username)
        .participantId(participantId)
        .requestUrl(requestUrl)
        .content(content)
        .eventType(eventType)
        .build();
  }
}
