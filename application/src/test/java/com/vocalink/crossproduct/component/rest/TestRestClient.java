package com.vocalink.crossproduct.component.rest;

import com.google.common.base.Joiner;
import com.vocalink.crossproduct.component.ScenarioContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile("component")
public class TestRestClient {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ScenarioContext context;

    public ResponseEntity<?> get(String path) {
        return get(path, defaultHttpHeaders());
    }

    public ResponseEntity<?> get(String path, Map<String, String> params) {
        HttpEntity<?> httpEntity = new HttpEntity<>(defaultHttpHeaders());
        String parametrizedPath = addParamsToPath(path, params);
        return sendAndLogRequest(defaultHttpHeaders(), parametrizedPath, HttpMethod.GET, httpEntity);
    }

    public ResponseEntity<?> get(String path, HttpHeaders httpHeaders) {
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        return sendAndLogRequest(httpHeaders, path, HttpMethod.GET, httpEntity);
    }

    public ResponseEntity<?> post(String path, String body) {
        return post(path, body, defaultHttpHeaders());
    }

    public ResponseEntity<?> post(String path, Object body, HttpHeaders httpHeaders) {
        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
        return sendAndLogRequest(httpHeaders, path, HttpMethod.POST, httpEntity);
    }

    public ResponseEntity<?> put(String path, String body) {
        return put(path, body, defaultHttpHeaders());
    }

    public ResponseEntity<?> put(String path, String body, HttpHeaders httpHeaders) {
        HttpEntity<?> httpEntity = new HttpEntity<>(body, httpHeaders);
        return sendAndLogRequest(httpHeaders, path, HttpMethod.PUT, httpEntity);
    }

    public ResponseEntity<?> put(String path) {
        HttpEntity<?> httpEntity = new HttpEntity<>(defaultHttpHeaders());
        return sendAndLogRequest(defaultHttpHeaders(), path, HttpMethod.PUT, httpEntity);
    }

    public ResponseEntity<?> put(String path, Map<String, String> params) {
        return put(path, params, defaultHttpHeaders());
    }

    public ResponseEntity<?> put(String path, Map<String, String> params, HttpHeaders httpHeaders) {
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        String parametrizedPath = addParamsToPath(path, params);
        return sendAndLogRequest(httpHeaders, parametrizedPath, HttpMethod.PUT, httpEntity);
    }

    public ResponseEntity<?> delete(String path) {
        return delete(path, defaultHttpHeaders());
    }

    public ResponseEntity<?> delete(String path, HttpHeaders httpHeaders) {
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        return sendAndLogRequest(httpHeaders, path, HttpMethod.DELETE, httpEntity);
    }

    public HttpHeaders defaultHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(Headers.CONTENT_TYPE, "application/json;charset=UTF-8");
        httpHeaders.set(Headers.CONTEXT, "BPS");
        httpHeaders.set(Headers.CLIENT_TYPE, "UI");
        httpHeaders.set(Headers.X_USER_ID_HEADER, "test-user-001");
        httpHeaders.set(Headers.X_PARTICIPANT_ID_HEADER, "HANDSESS");
        httpHeaders.set(Headers.X_ROLES_HEADER, "MANAGEMENT");
        return httpHeaders;
    }

    private ResponseEntity<?> sendAndLogRequest(HttpHeaders httpHeaders, String path, HttpMethod httpMethod, HttpEntity<?> httpEntity) {
        try {
            log.info("#");
            log.info("Sending: {} request on path: {} ", httpMethod, path);
            log.info("Sending: with headers: {}", httpHeaders);
            log.info("Sending: with body: {} ", httpEntity.getBody());

            ResponseEntity<?> responseEntity = restTemplate.exchange(path, httpMethod, httpEntity, String.class);
            log.info("Received: status code: {}, headers: {}",
                     responseEntity.getStatusCode().value(),
                     responseEntity.getHeaders(),
                     responseEntity.getBody());
            log.info("Received: body {}", responseEntity.getBody());
            log.info("Received: headers {}", responseEntity.getHeaders());
            context.addResponse(responseEntity);
            return responseEntity;
        } catch (HttpClientErrorException | HttpServerErrorException cx) {
            log.error("Got error: {}, {}, {}", cx.getStatusCode(), cx.getStatusText(), cx.getResponseBodyAsString());
            ResponseEntity<String> responseEntity = new ResponseEntity<>(cx.getResponseBodyAsString(), cx.getStatusCode());
            context.addResponse(responseEntity);
            return responseEntity;
        } finally {
            context.addRequest(new RequestEntity<>(httpHeaders, httpMethod, URI.create(path)));
        }
    }

    private String addParamsToPath(String path, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        List<String> p = new ArrayList<>();
        params.forEach((k, v) -> p.add(String.format("%s=%s", k, v)));
        return sb.append(path).append("?").append(Joiner.on("&").join(p)).toString().replace(" ", "");
    }

}
