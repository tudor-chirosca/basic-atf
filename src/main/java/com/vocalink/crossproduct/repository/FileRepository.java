package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.FileEnquiry;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import java.util.List;

public interface FileRepository {

  @Deprecated
  List<FileReference> findFileReferences(String context);

  List<FileReference> findFileReferences(String context, String enquiryType);

  Page<FileEnquiry> findFileEnquiries(String context, FileEnquirySearchRequest request);
}
