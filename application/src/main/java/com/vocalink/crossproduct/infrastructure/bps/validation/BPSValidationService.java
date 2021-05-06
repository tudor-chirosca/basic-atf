package com.vocalink.crossproduct.infrastructure.bps.validation;

import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.domain.validation.ValidationService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BPSValidationService implements ValidationService {

  private static final String START_EOD_PERIOD_TIME = "start";
  private static final String END_EOD_PERIOD_TIME = "end";
  private static final String ZONE = "UTC";

  private final BPSProperties bpsProperties;

  @Override
  public ValidationApproval getApprovalValidation(ZonedDateTime currentTime) {
    final Map<String, String> eodPeriod = bpsProperties.getEodPeriod();

    final ZonedDateTime start = ZonedDateTime.of(LocalDateTime
            .of(LocalDate.now(ZoneId.of(ZONE)), LocalTime.parse(eodPeriod.get(START_EOD_PERIOD_TIME))), ZoneId.of(ZONE));

    final ZonedDateTime end = ZonedDateTime.of(LocalDateTime
            .of(LocalDate.now(ZoneId.of(ZONE)), LocalTime.parse(eodPeriod.get(END_EOD_PERIOD_TIME))), ZoneId.of(ZONE));

    final boolean isAvailable = currentTime.isBefore(start) || currentTime.isAfter(end);

    return new ValidationApproval(isAvailable, currentTime);
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
