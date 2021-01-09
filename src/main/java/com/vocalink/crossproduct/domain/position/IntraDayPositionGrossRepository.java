package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface IntraDayPositionGrossRepository extends CrossproductRepository {

  List<IntraDayPositionGross> findIntraDayPositionGrossByParticipantIds(List<String> participantIds);

}
