package com.vocalink.crossproduct.domain.reference;

import java.util.List;

public interface ReferencesRepository {

  List<MessageDirectionReference> findMessageDirectionReferences(String context);
}
