package com.vocalink.crossproduct.ui.dto.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ApprovalUserDto {

  final private String name;
  final private String id;
  final private String participantName;
}
