package com.vocalink.crossproduct.ui.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ContentUtils {

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

  public Object toObject(String content, EventType eventType) {
    try {
      return objectMapper.readValue(content, eventType.getRequestType());
    } catch (JsonProcessingException e) {
      throw new UILayerException(e, "Could not deserialize request message");
    }
  }
}
