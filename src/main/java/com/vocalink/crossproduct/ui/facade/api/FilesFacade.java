package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import org.springframework.core.io.Resource;

public interface FilesFacade {

  PageDto<FileDto> getPaginated(String product, ClientType clientType,
      FileEnquirySearchRequest request);

  FileDetailsDto getDetailsById(String product, ClientType clientType, String id);

  Resource getFile(String product, ClientType clientType, String fileId);
}
