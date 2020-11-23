package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquiryDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface FileEnquiriesFacade {

  PageDto<FileEnquiryDto> getFileEnquiries(String context, ClientType clientType,
      FileEnquirySearchRequest request);
}
