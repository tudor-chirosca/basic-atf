package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FilesFacadeImpl implements FilesFacade {

  private final PresenterFactory presenterFactory;
  private final FileRepository fileRepository;

  @Override
  public PageDto<FileDto> getFiles(String context, ClientType clientType,
      FileEnquirySearchRequest request) {

    final Page<File> files = fileRepository.findFilesPaginated(context, request);

    return presenterFactory.getPresenter(clientType).presentFiles(files);
  }

  @Override
  public FileDetailsDto getDetailsById(String context, ClientType clientType, String id) {

    final File file = fileRepository.findFileById(context, id);

    return presenterFactory.getPresenter(clientType).presentFileDetails(file);
  }
}
