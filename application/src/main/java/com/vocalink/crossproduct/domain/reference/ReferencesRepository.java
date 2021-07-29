package com.vocalink.crossproduct.domain.reference;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface ReferencesRepository extends CrossproductRepository {

  List<MessageDirectionReference> findMessageDirectionReferences();

  ReasonCodeReference findReasonCodeReference();

  List<String> findStatuses(String enquiryType);

  List<OutputFlowReference> findOutputFlowReferences();

}
