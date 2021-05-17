package com.vocalink.crossproduct.infrastructure.bps.service;

import java.io.OutputStream;
import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import com.vocalink.crossproduct.domain.ResourceService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BPSResourceService implements ResourceService {

    private final BPSProperties bpsProperties;
    private final RestTemplate restTemplate;

    public void writeResourceToOutputStream(String path, String id, OutputStream outputStream) {
        log.info("Fetching from: {}, with id: {}", path, id);
        restTemplate.execute(resolve(path, bpsProperties, bpsProperties.getSchemeCode(), id), HttpMethod.POST,
                request -> request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM)),
                response -> {
                    if (!response.getStatusCode().isError()) {
                        StreamUtils.copy(response.getBody(), outputStream);
                    }
                    return null;
                });
    }

    @Override
    public String getProduct() {
        return BPSConstants.PRODUCT;
    }
}
