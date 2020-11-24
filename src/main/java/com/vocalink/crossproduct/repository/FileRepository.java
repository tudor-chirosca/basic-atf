package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileDetails;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import java.util.List;

public interface FileRepository {

  @Deprecated
  List<FileReference> findFileReferences(String context);

  List<FileReference> findFileReferences(String context, String enquiryType);

  Page<File> findFiles(String context, FileEnquirySearchRequest request);

  List<FileDetails> findDetailsBy(String context, List<String> ids);
}
