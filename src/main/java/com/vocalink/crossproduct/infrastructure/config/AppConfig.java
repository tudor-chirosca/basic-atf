package com.vocalink.crossproduct.infrastructure.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public ModelMapper modelMapper() {

    ModelMapper modelMapper = new ModelMapper();

    Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
      @Override
      public LocalDate get() {
        return LocalDate.now();
      }
    };
    Converter<String, LocalDate> stringLocalDateConverter = new AbstractConverter<String, LocalDate>() {
      @Override
      protected LocalDate convert(String source) {
        return source == null ? null : LocalDate.parse(source);
      }
    };

    modelMapper.createTypeMap(String.class, LocalDate.class);
    modelMapper.addConverter(stringLocalDateConverter);
    modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);

    Provider<LocalDateTime> localDateTimeProvider = new AbstractProvider<LocalDateTime>() {
      @Override
      public LocalDateTime get() {
        return LocalDateTime.now();
      }
    };

    Converter<String, LocalDateTime> stringLocalDateTimeConverter = new AbstractConverter<String, LocalDateTime>() {
      @Override
      protected LocalDateTime convert(String source) {
        return source == null ? null : LocalDateTime.parse(source);
      }
    };

    modelMapper.createTypeMap(String.class, LocalDateTime.class);
    modelMapper.addConverter(stringLocalDateTimeConverter);
    modelMapper.getTypeMap(String.class, LocalDateTime.class).setProvider(localDateTimeProvider);

    return modelMapper;
  }
}
