package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.FileEnquiry;
import com.vocalink.crossproduct.repository.FileRepository;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquiryDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FileEnquiriesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileEnquiriesFacadeImpl implements FileEnquiriesFacade {

  private final PresenterFactory presenterFactory;
  private final FileRepository fileRepository;

  @Override
  public PageDto<FileEnquiryDto> getFileEnquiries(String context, ClientType clientType,
      FileEnquirySearchRequest request) {

    Page<FileEnquiry> fileEnquiries = fileRepository.findFileEnquiries(context, request);

    return presenterFactory.getPresenter(clientType).presentEnquiries(fileEnquiries);
  }
}
