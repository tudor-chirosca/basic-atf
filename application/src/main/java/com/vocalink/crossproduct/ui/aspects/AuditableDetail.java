package com.vocalink.crossproduct.ui.aspects;

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
}
