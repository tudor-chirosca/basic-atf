package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface FilesFacade {

  PageDto<FileDto> getPaginated(String product, ClientType clientType,
      FileEnquirySearchRequest request);

  FileDetailsDto getDetailsById(String product, ClientType clientType, String id);

}
