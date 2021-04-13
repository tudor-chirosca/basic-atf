package com.vocalink.crossproduct.ui.aspects;

import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_DETAILS_ENQUIRY;
import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_SEARCH_ENQUIRY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ContentUtils {

  private static final Map<EventType, Class<?>> eventTypeClassMap = new HashMap<>();

  static {
    eventTypeClassMap.put(FILE_SEARCH_ENQUIRY, FileEnquirySearchRequest.class);
    eventTypeClassMap.put(FILE_DETAILS_ENQUIRY, String.class);
  }

  private final ObjectMapper objectMapper;

  @Autowired
  public ContentUtils(@Qualifier("objectMapper") ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String toJsonString(Object content) {
    try {
      return objectMapper.writeValueAsString(content);
    } catch (JsonProcessingException e) {
      throw new UILayerException(e, "Could not deserialize incoming request message");
    }
  }

  public Object toObject(String content, EventType eventType, OperationType operationType) {
    if (operationType.equals(OperationType.RESPONSE)) {
      return content;
    }

    final Class<?> targetClass = eventTypeClassMap.get(eventType);

    try {
      return objectMapper.readValue(content, targetClass);
    } catch (JsonProcessingException e) {
      throw new UILayerException(e, "Could not deserialize request message");
    }
  }
}
