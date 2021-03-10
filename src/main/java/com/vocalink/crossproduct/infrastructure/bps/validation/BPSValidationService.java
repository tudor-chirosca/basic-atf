package com.vocalink.crossproduct.infrastructure.bps.validation;

import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.domain.validation.ValidationService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BPSValidationService implements ValidationService {

  private static final String START_EOD_PERIOD_TIME = "start";
  private static final String END_EOD_PERIOD_TIME = "end";

  private final BPSProperties bpsProperties;

  @Override
  public ValidationApproval getApprovalValidation(ZonedDateTime currentTime) {
    final Map<String, String> eodPeriod = bpsProperties.getEodPeriod();

    boolean value = currentTime.isBefore(currentTime.withHour(parseInt(eodPeriod.get(START_EOD_PERIOD_TIME)))) ||
        currentTime.isAfter(currentTime.withHour(parseInt(eodPeriod.get(END_EOD_PERIOD_TIME))));

    return new ValidationApproval(value, currentTime);
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
