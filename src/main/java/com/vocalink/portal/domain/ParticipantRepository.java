package com.vocalink.portal.domain;

import reactor.core.publisher.Mono;

public interface ParticipantRepository {

  Mono<Participant[]> fetchParticipants();
}
