package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import java.util.Optional;

public interface IntraDayPositionGrossRepository {

  Optional<IntraDayPositionGross> findIntraDayPositionGrossByParticipantId(String context, String participantId);

}
