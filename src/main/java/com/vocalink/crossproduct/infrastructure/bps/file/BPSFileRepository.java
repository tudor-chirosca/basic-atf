package com.vocalink.crossproduct.infrastructure.bps.file;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.FILE_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.FILE_REFERENCES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_FILE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.files.FileRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSFileRepository implements FileRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<FileReference> findFileReferences() {
    return webClient.post()
        .uri(resolve(FILE_REFERENCES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToFlux(BPSFileReference.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public Page<File> findBy(FileEnquirySearchCriteria request) {
    final BPSFileEnquirySearchRequest bpsRequest = BPSMAPPER.toBps(request);
    final URI uri = UriComponentsBuilder.fromUri(resolve(FILE_ENQUIRIES_PATH, bpsProperties))
        .queryParam(OFFSET, request.getOffset())
        .queryParam(PAGE_SIZE, request.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSFileEnquirySearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSFile>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(f -> MAPPER.toEntity(f, File.class))
        .block();
  }

  @Override
  public File findById(String id) {
    final BPSSingleFileRequest bpsRequest = new BPSSingleFileRequest(null, id);
    return webClient.post()
        .uri(resolve(SINGLE_FILE_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSingleFileRequest.class))
        .retrieve()
        .bodyToMono(BPSFile.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
