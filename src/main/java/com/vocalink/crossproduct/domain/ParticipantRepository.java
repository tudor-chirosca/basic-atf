package com.vocalink.crossproduct.domain;


import java.util.List;

public interface ParticipantRepository {
    List<Participant> findAll(String context);
}
