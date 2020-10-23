package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import java.util.List;
import java.util.Optional;

public interface IntraDayPositionGrossRepository {

  List<IntraDayPositionGross> findIntraDayPositionGrossByParticipantId(String context, List<String> participantId);

}
