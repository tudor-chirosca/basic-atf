package com.vocalink.crossproduct.infrastructure.bps.file;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DOWNLOAD_FILE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.FILE_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.FILE_REFERENCES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_FILE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.files.FileRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BPSFileRepository implements FileRepository {

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
        .map(BPSMAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public Page<File> findPaginated(FileEnquirySearchCriteria request) {
    BPSFileEnquirySearchRequest bpsRequest = BPSMAPPER.toBps(request);
    return webClient.post()
        .uri(resolve(FILE_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSFileEnquirySearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSFile>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toFilePageEntity)
        .block();
  }

  @Override
  public File findById(String id) {
    BPSSingleFileRequest bpsRequest = new BPSSingleFileRequest(id);
    return webClient.post()
        .uri(resolve(SINGLE_FILE_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSingleFileRequest.class))
        .retrieve()
        .bodyToMono(BPSFile.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(BPSMAPPER::toEntity)
        .block();
  }

  @Override
  public InputStream getFileById(String id) throws IOException {

    PipedOutputStream outputPipe = new PipedOutputStream();
    PipedInputStream inputPipe = new PipedInputStream(outputPipe);

    final Flux<DataBuffer> body = webClient.post()
        .uri(resolve(DOWNLOAD_FILE_PATH, bpsProperties).toString(), b -> b.pathSegment(id).build())
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        .accept(MediaType.APPLICATION_OCTET_STREAM)
        .exchange()
        .flatMapMany(r -> r.body(BodyExtractors.toDataBuffers()))
        .doOnError(t -> {
          log.error("Error reading body.", t);
          try {
            inputPipe.close();
          } catch (IOException ioe) {
            log.error("Error closing input stream", ioe);
          }
        })
        .doFinally(s -> {
          try {
            outputPipe.close();
          } catch (IOException ioe) {
            log.error("Error closing output stream", ioe);
          }
        });

    DataBufferUtils.write(body, outputPipe)
        .subscribe(DataBufferUtils.releaseConsumer());

    return inputPipe;
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }

}
