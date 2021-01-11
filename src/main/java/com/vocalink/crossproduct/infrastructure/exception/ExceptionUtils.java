package com.vocalink.crossproduct.infrastructure.exception;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants.PRODUCT;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtils {

  public static void raiseException(Throwable throwable) {
    throw new InfrastructureException(throwable, PRODUCT);
  }
}
