package com.vocalink.crossproduct.domain.position;

import java.util.List;

public interface IntraDayPositionGrossRepository {

  List<IntraDayPositionGross> findIntraDayPositionGrossByParticipantId(String context, List<String> participantId);

}
