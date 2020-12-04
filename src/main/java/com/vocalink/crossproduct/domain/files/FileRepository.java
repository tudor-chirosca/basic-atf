package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import java.util.List;

public interface FileRepository {

  List<FileReference> findFileReferences(String context, String enquiryType);

  Page<File> findFilesPaginated(String context, FileEnquirySearchRequest request);

  File findFileById(String context, String id);
}
