package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.BATCH_CANCELLATION;
import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.CONFIG_CHANGE;
import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.TRANSACTION_CANCELLATION;

import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidChangeRequestValidator implements
    ConstraintValidator<ValidChangeRequest, ApprovalChangeRequest> {

  public static final String ID = "id";
  public static final String BATCH_ID = "batchId";
  public static final String TRANSACTION_ID = "transactionId";
  public static final String DEBIT_CAP_LIMIT = "debitCapLimit";
  public static final String VALUE_BETWEEN_1_AND_99_BILLION = "Please enter a value between 1 and 99 billion";
  public static final String NUMBER_SHOULD_NOT_BE_DECIMAL = "Inserted number should not be Decimal";
  public static final String CONTAIN_BATCH_ID = "Batch cancellation request should contain batch id";
  public static final String CONTAIN_TRANSACTION_ID = "Transaction cancellation request should contain transaction id";
  public static final String CONTAIN_ID = "This request should contain id";

  @Override
  public boolean isValid(ApprovalChangeRequest model, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    if(model.getRequestedChange() != null) {
      return isValidRequestedChange(model, context);
    }
    return true;
  }

  private boolean isValidRequestedChange(ApprovalChangeRequest model,
      ConstraintValidatorContext context) {
    final Map<String, Object> requestedChange = model.getRequestedChange();
    final ApprovalRequestType requestType = ApprovalRequestType.valueOf(model.getRequestType());
    final Object object = model.getRequestedChange().get(DEBIT_CAP_LIMIT);
    if (BATCH_CANCELLATION.equals(requestType)) {
      return hasRequiredId(requestedChange.containsKey(BATCH_ID), context, CONTAIN_BATCH_ID);
    }
    if (TRANSACTION_CANCELLATION.equals(requestType)) {
      return hasRequiredId(requestedChange.containsKey(TRANSACTION_ID), context,
          CONTAIN_TRANSACTION_ID);
    }
    if (CONFIG_CHANGE.equals(requestType)) {
      return hasRequiredId(requestedChange.containsKey(ID), context, CONTAIN_ID) &&
          isValidDebitCap(object, context);
    }
    return hasRequiredId(requestedChange.containsKey(ID), context, CONTAIN_ID);
  }

  private boolean hasRequiredId(boolean hasIdKey,
      ConstraintValidatorContext context, String errorMessage) {
    if (hasIdKey) {
      return true;
    }
    customMessageForValidation(context, errorMessage);
    return false;
  }

  private boolean isValidDebitCap(Object debitCap, ConstraintValidatorContext context) {
    if (debitCap instanceof Long) {
      customMessageForValidation(context, VALUE_BETWEEN_1_AND_99_BILLION);
      Long longDebitCap = (Long) debitCap;
      return longDebitCap <= 99000000000L;
    } else if (debitCap instanceof Integer) {
      customMessageForValidation(context, VALUE_BETWEEN_1_AND_99_BILLION);
      Integer integerDebitCap = (Integer) debitCap;
      return integerDebitCap > 0;
    }
    customMessageForValidation(context, NUMBER_SHOULD_NOT_BE_DECIMAL);
    return false;
  }

  private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
    constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }

  @Override
  public void initialize(ValidChangeRequest constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
