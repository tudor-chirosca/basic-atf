package com.vocalink.portal.domain;

import reactor.core.publisher.Mono;

public interface CycleRepository {

  Mono<Cycle[]> fetchCycles();
}
