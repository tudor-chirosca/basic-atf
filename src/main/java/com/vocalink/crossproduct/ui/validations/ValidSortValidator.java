package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.validations.ValidatorUtils.tryGetFieldValue;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;

public class ValidSortValidator implements
    ConstraintValidator<ValidSort, Object> {

  private String sortFieldName;
  private List<String> validSortParams;

  @Override
  public void initialize(ValidSort constraintAnnotation) {
    this.sortFieldName = constraintAnnotation.sort();
    this.validSortParams = asList(constraintAnnotation.sortingKeys());
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    ArrayList<String> rawSorts = tryGetFieldValue(obj, sortFieldName,
        ArrayList.class);
    if (rawSorts == null || rawSorts.isEmpty()) {
      return true;
    }
    List<String> sorts = rawSorts.stream()
        .map(s -> s.replaceAll("[_+-]", "").trim())
        .collect(toList());

    return validSortParams.containsAll(sorts);
  }
}
