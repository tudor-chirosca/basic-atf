package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilesFacadeImpl implements FilesFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public PageDto<FileDto> getPaginated(String product, ClientType clientType,
      FileEnquirySearchRequest requestDto) {
    log.info("Fetching files from: {}", product);

    final FileEnquirySearchCriteria request = MAPPER.toEntity(requestDto);
    final Page<File> files = repositoryFactory.getFileRepository(product).findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentFiles(files);
  }

  @Override
  public FileDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching file details for id: {} from: {}", id, product);

    final File file = repositoryFactory.getFileRepository(product).findById(id);

    return presenterFactory.getPresenter(clientType).presentFileDetails(file);
  }
}
