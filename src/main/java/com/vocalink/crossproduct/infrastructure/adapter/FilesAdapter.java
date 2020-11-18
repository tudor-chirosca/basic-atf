package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.FileEnquiry;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.FileRepository;
import com.vocalink.crossproduct.shared.files.CPFileEnquirySearchRequest;
import com.vocalink.crossproduct.shared.files.FilesClient;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilesAdapter implements FileRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<FileReference> findFileReferences(String context) {

    final FilesClient filesClient = clientFactory.getFilesClient(context);
    log.info("Fetching all file references from: {}", context);

    return filesClient.findFileReferences()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public Page<FileEnquiry> findFileEnquiries(String context,
      FileEnquirySearchRequest request) {

    final FilesClient filesClient = clientFactory.getFilesClient(context);
    log.info("Fetching all file enquiries from: {}", context);

    CPFileEnquirySearchRequest cpRequest = MAPPER.toCp(request);

    return MAPPER.toEntity(filesClient.findFileEnquiries(cpRequest));
  }
}
