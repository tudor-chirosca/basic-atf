package com.vocalink.crossproduct.infrastructure.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.cache.CacheManager;
import javax.cache.Caching;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

  @Value("${cache.onheap.size}")
  private Integer onHeapSize;

  @Value("${cache.offheap.size}")
  private Integer offHeapSize;

  @Value("${cache.expiry.time}")
  private Integer expiryTime;

  @Bean
  public JCacheCacheManager jCacheCacheManager() {
    return new JCacheCacheManager(cacheManager());
  }

  @Bean(destroyMethod = "close")
  public CacheManager cacheManager() {
    EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching
        .getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");

    ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder()
        .heap(onHeapSize, EntryUnit.ENTRIES)
        .offheap(offHeapSize, MemoryUnit.MB)
        .build();

    CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
        .newCacheConfigurationBuilder(
            Object.class,
            Object.class,
            resourcePools)
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(expiryTime)))
        .build();

    Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
    caches.put("participantCache", cacheConfiguration);

    org.ehcache.config.Configuration configuration = new DefaultConfiguration(caches,
        provider.getDefaultClassLoader());

    return provider.getCacheManager(provider.getDefaultURI(), configuration);
  }
}
