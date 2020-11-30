package com.vocalink.crossproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class InternationalSuiteServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InternationalSuiteServiceApplication.class, args);
  }

}
