package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.FileDetails;
import com.vocalink.crossproduct.domain.files.FileEnquiry;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.repository.FileRepository;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquiryDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FilesFacadeImpl implements FilesFacade {

  private final PresenterFactory presenterFactory;
  private final FileRepository fileRepository;

  @Override
  public PageDto<FileEnquiryDto> getFileEnquiries(String context, ClientType clientType,
      FileEnquirySearchRequest request) {

    final Page<FileEnquiry> fileEnquiries = fileRepository.findFileEnquiries(context, request);

    return presenterFactory.getPresenter(clientType).presentEnquiries(fileEnquiries);
  }

  @Override
  public FileDetailsDto getDetailsById(String context, ClientType clientType, String id) {
    final FileDetails fileDetails = fileRepository.findDetailsBy(context, singletonList(id)).stream()
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException("There is no file with such an id"));

    return presenterFactory.getPresenter(clientType).presentFileDetails(fileDetails);
  }
}
