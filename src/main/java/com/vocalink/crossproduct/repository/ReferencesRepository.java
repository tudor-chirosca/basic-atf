package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import java.util.List;

public interface ReferencesRepository {

  List<MessageDirectionReference> getMessageDirectionReferences(String context);
}
