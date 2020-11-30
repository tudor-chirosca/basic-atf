package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface BatchesFacade {

  PageDto<BatchDto> getBatches(String context, ClientType clientType,
      BatchEnquirySearchRequest request);

}
