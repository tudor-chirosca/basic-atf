package com.vocalink.crossproduct.domain.audit;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDetails {

  private final String userId;
  private final String firstName;
  private final String lastName;
  private final String participantId;

  public String getFullName() {
    return new StringBuffer()
        .append(firstName == null ? EMPTY : firstName)
        .append(firstName == null || lastName == null ? EMPTY : SPACE)
        .append(lastName == null ? EMPTY : lastName)
        .toString();
  }
}
