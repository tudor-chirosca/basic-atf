package com.vocalink.crossproduct.infrastructure.config;

import com.vocalink.bps.interceptors.DNFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO: To configure tomcatDNList

@Configuration
@ConditionalOnProperty(value = "mc.dn.filter.enabled", havingValue = "true")
public class MCDNFilterConfig {

  @Bean
  public FilterRegistrationBean<DNFilter> getFilterRegistrationBean() {
    final FilterRegistrationBean<DNFilter> dnFilterBean = new FilterRegistrationBean<>();
    dnFilterBean.setFilter(new DNFilter());
    dnFilterBean.addUrlPatterns("/*");
    dnFilterBean.setName("DnValidationFilter");
    dnFilterBean.setOrder(1);
    return dnFilterBean;
  }
}
