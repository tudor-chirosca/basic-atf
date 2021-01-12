package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface FileRepository extends CrossproductRepository {

  List<FileReference> findFileReferences(String enquiryType);

  Page<File> findPaginated(FileEnquirySearchCriteria criteria);

  File findById(String id);
}
