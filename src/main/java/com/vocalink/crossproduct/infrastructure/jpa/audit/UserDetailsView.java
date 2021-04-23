package com.vocalink.crossproduct.infrastructure.jpa.audit;

public interface UserDetailsView {

  String getUsername();

  String getFirstName();

  String getLastName();

  String getParticipantId();
}
