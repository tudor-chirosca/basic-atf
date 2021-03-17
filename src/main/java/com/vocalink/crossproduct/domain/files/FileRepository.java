package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface FileRepository extends CrossproductRepository {

  Page<File> findBy(FileEnquirySearchCriteria criteria);

  File findById(String id);
}
