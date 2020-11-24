package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface FilesFacade {

  PageDto<FileDto> getFiles(String context, ClientType clientType,
      FileEnquirySearchRequest request);

  FileDetailsDto getDetailsById(String context, ClientType clientType, String id);

}
