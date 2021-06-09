package com.vocalink.crossproduct.ui.aspects;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class AuditableDetail {

  private String jobId;

  private Map<String, Object> previousValues;

  public void setPreviousValues(Map<String, Object> values) {
    previousValues = new HashMap<>();
    previousValues.put("previousValues", values);
  }
}
