package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DOWNLOAD_FILE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileRepository;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import com.vocalink.crossproduct.ui.facade.api.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilesFacadeImpl implements FilesFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public PageDto<FileDto> getPaginated(String product, ClientType clientType,
      FileEnquirySearchRequest requestDto) {
    log.info("Fetching files from: {}", product);

    final FileEnquirySearchCriteria request = MAPPER.toEntity(requestDto);

    final Result<File> result = repositoryFactory.getFileRepository(product).findBy(request);

    return presenterFactory.getPresenter(clientType)
        .presentFiles(result.getSummary().getTotalCount(), result.getData());
  }

  @Override
  public FileDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching file details for id: {} from: {}", id, product);

    final File file = repositoryFactory.getFileRepository(product)
        .findById(id);
    final Participant sender = repositoryFactory.getParticipantRepository(product)
        .findById(file.getOriginator());
    final Account account = repositoryFactory.getAccountRepository(product)
        .findByPartyCode(sender.getBic());

    return presenterFactory.getPresenter(clientType).presentFileDetails(file, sender, account);
  }

  @Override
  public Resource getFile(String product, ClientType clientType, String fileId) {
    try {
      final InputStream input = serviceFactory.getDownloadService(product)
          .getResource(DOWNLOAD_FILE_PATH, fileId);
      return presenterFactory.getPresenter(clientType).presentStream(input);
    } catch (IOException e) {
      throw new UILayerException(e, "Exception thrown while reading input stream.");
    }
  }
}
