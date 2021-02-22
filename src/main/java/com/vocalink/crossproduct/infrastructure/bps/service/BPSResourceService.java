package com.vocalink.crossproduct.infrastructure.bps.service;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.vocalink.crossproduct.domain.ResourceService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

@Slf4j
@Component
@RequiredArgsConstructor
public class BPSResourceService implements ResourceService {

  private final BPSProperties bpsProperties;
  private final WebClient webClient;

  public InputStream getResource(String path, String id) throws IOException {
    log.info("Fetching from: {}, with id: {}", path, id);

    PipedOutputStream outputPipe = new PipedOutputStream();
    PipedInputStream inputPipe = new PipedInputStream(outputPipe);

    final Flux<DataBuffer> body = webClient.post()
        .uri(resolve(path, bpsProperties).toString(), b -> b.pathSegment(id).build())
        .header(ACCEPT, APPLICATION_OCTET_STREAM_VALUE)
        .retrieve()
        .bodyToFlux(DataBuffer.class)
        .doOnError(t -> {
          log.debug("Error reading body.", t);
          try {
            inputPipe.close();
          } catch (IOException ioe) {
            log.debug("Error closing input stream", ioe);
          }
        })
        .doFinally(s -> {
          try {
            outputPipe.close();
          } catch (IOException ioe) {
            log.debug("Error closing output stream", ioe);
          }
        });

    DataBufferUtils.write(body, outputPipe)
        .subscribe(DataBufferUtils.releaseConsumer());

    Hooks.onErrorDropped(error -> log.debug(error.getMessage()));

    return inputPipe;
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
