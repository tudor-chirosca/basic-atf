package com.vocalink.crossproduct.ui.dto.participant;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET;
import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class ManagedParticipantsSearchRequest {

  private int offset = parseInt(getDefault(OFFSET));
  private int limit = parseInt(getDefault(LIMIT));
  private List<String> sort;
  private String q;
}
