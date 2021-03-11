package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface FileRepository extends CrossproductRepository {

  List<FileReference> findFileReferences();

  Page<File> findBy(FileEnquirySearchCriteria criteria);

  File findById(String id);
}
