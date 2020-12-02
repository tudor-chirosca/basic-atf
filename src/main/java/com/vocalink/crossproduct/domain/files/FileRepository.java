package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import java.util.List;

public interface FileRepository {

  @Deprecated
  List<FileReference> findFileReferences(String context);

  List<FileReference> findFileReferences(String context, String enquiryType);

  Page<File> findFilesPaginated(String context, FileEnquirySearchRequest request);

  List<File> findFilesByIds(String context, List<String> ids);
}
