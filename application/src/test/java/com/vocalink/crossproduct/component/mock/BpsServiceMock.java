package com.vocalink.crossproduct.component.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
@Profile("component")
public class BpsServiceMock {

    @Value("${bps-service-mock.port.http}")
    private int httpPort;

    @Value("${bps-service-mock.port.https}")
    private int httpsPort;

    @Value("${bps-service-mock.port.dynamic}")
    private boolean dynamicPorts;

    private WireMockServer mockServer;

    @PostConstruct
    private void init() {
        if (dynamicPorts) {
            mockServer = new WireMockServer(wireMockConfig().dynamicPort().dynamicHttpsPort());
            httpPort = mockServer.port();
            httpsPort = mockServer.httpsPort();
        } else {
            mockServer = new WireMockServer(wireMockConfig().port(httpPort).httpsPort(httpsPort));
        }
        mockServer.start();
        log.info("Wiremock running on: " + mockServer.baseUrl() + ", " + mockServer.httpsPort() + ", " + mockServer.port());
    }

    public void reset() {
        mockServer.resetAll();
    }

    @PreDestroy
    private void stop() {
        mockServer.stop();
    }

    public StubMapping givenThat(MappingBuilder mappingBuilder) {
        return mockServer.givenThat(mappingBuilder);
    }

    public void acceptAllIncomingRequests() {
        givenThat(post(anyUrl()).willReturn(ok())).setPriority(5);
    }

    public void acceptPostRequestsOnPath(String path, String body) {
        givenThat(post(urlPathMatching(path))
                                  .willReturn(aResponse().withHeader("Content-Type", "application/json;charset=UTF-8")
                                                         .withBody(body)));
    }

    public LoggedRequest requireOneIncomingRequestAsap(String urlPattern) {
        return requireOneIncomingRequest(urlPattern, 10);
    }

    public LoggedRequest requireOneIncomingRequest(String urlPattern, int withinSec) {
        return retryFailOnTimeout(() -> getFirstIncomingRequest(urlPattern), Objects::nonNull, withinSec, "No incoming request, url=" + urlPattern);
    }

    public LoggedRequest getFirstIncomingRequest(String urlPattern) {
        List<LoggedRequest> req = getAllIncomingRequests(urlPattern);
        return CollectionUtils.isEmpty(req) ? null : req.get(0);
    }

    public List<LoggedRequest> getAllIncomingRequests(String urlPattern) {
        return getAllIncomingRequests(postRequestedFor(urlMatching(urlPattern)));
    }

    public List<LoggedRequest> getAllIncomingRequests(RequestPatternBuilder requestPatternBuilder) {
        return mockServer.findAll(requestPatternBuilder);
    }

    private static <T> T retryFailOnTimeout(Supplier<T> action, Predicate<T> haltCondition, int withinSec, String description) {
        Optional<T> opt = retry(action, haltCondition, withinSec);
        return opt.orElseThrow(() -> new AssertionError(format("Can't obtain the desired result within %d seconds. %s", withinSec, description)));
    }

    private static <T> Optional<T> retry(Supplier<T> action, Predicate<T> haltCondition, int withinSec) {
        long currentTime = System.currentTimeMillis();
        long stopTime = currentTime + Math.max(withinSec, 0) * 1000L;
        while (true) {
            T result = action.get();
            if (haltCondition.test(result)) {
                return Optional.of(result);
            } else if (currentTime < stopTime) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException skip) {
                    skip.printStackTrace();
                }
                currentTime = System.currentTimeMillis();
            } else {
                break;
            }
        }
        return Optional.empty();
    }
}
