package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.files.FileReference;
import java.util.List;

public interface FileRepository {

  List<FileReference> findFileReferences(String context);

}
