package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileRepository extends CrossproductRepository {

  List<FileReference> findFileReferences();

  Page<File> findPaginated(FileEnquirySearchCriteria criteria);

  File findById(String id);

  InputStream getFileById(String fileId) throws IOException;
}
