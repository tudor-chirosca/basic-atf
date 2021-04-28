package com.vocalink.crossproduct.infrastructure.config;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

  private static final String X_USER_ID_HEADER = "x-user-id";

  @Value("${app.filter.rate-limit.endpoints}")
  private String[] rateLimitEndpoints;

  @Value("${app.filter.requests.per-min}")
  private Integer maxRequestsPerMinute;

  @Value("${app.filter.cache-exp.minutes}")
  private Integer cacheExpiryTime;

  private LoadingCache<String, Integer> requestCountsPerUser;

  @PostConstruct
  public void init() {
    requestCountsPerUser = CacheBuilder.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(cacheExpiryTime))
        .build(new CacheLoader<String, Integer>() {
          @Override
          public Integer load(String key) {
            return 0;
          }
        });
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {

    final String userId = request.getHeader(X_USER_ID_HEADER);
    final String requestPath = request.getServletPath();

    try {
      int requestUserCounts = requestCountsPerUser.get(userId);
      int requestPathCounts = requestCountsPerUser.get(requestPath);
      if (requestUserCounts >= maxRequestsPerMinute && requestPathCounts >= maxRequestsPerMinute) {
        throw new ClientRequestException(TOO_MANY_REQUESTS,
            "Too many request per " + cacheExpiryTime + " minute");
      }
      requestCountsPerUser.put(userId, ++requestUserCounts);
      requestCountsPerUser.put(requestPath, ++requestPathCounts);
    } catch (ExecutionException e) {
      throw new InfrastructureException(e, "Can not get value by key: " + userId + " because of: " +
          e.getCause());
    }
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(rateLimitEndpoints)
        .noneMatch(e -> request.getServletPath().matches(e));
  }
}
