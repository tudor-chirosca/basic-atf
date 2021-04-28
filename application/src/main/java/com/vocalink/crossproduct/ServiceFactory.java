package com.vocalink.crossproduct;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.domain.ResourceService;
import com.vocalink.crossproduct.domain.approval.ApprovalService;
import com.vocalink.crossproduct.domain.configuration.ConfigurationService;
import com.vocalink.crossproduct.domain.exception.ServiceNotAvailableException;
import com.vocalink.crossproduct.domain.validation.ValidationService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceFactory {

  private final List<ApprovalService> approvalServices;
  private final List<ResourceService> resourceServices;
  private final List<ValidationService> validationServices;
  private final List<ConfigurationService> configurationServices;

  private Map<String, ApprovalService> approvalServicesByProduct;
  private Map<String, ResourceService> downloadServicesByProduct;
  private Map<String, ValidationService> validationServicesByProduct;
  private Map<String, ConfigurationService> configurationServicesByProduct;

  @PostConstruct
  public void init() {
    downloadServicesByProduct = resourceServices.stream()
        .collect(toMap(ResourceService::getProduct, Function.identity()));
    approvalServicesByProduct = approvalServices.stream()
        .collect(toMap(ApprovalService::getProduct, Function.identity()));
    validationServicesByProduct = validationServices.stream()
        .collect(toMap(ValidationService::getProduct, Function.identity()));
    configurationServicesByProduct = configurationServices.stream()
        .collect(toMap(ConfigurationService::getProduct, Function.identity()));
  }

  public ResourceService getDownloadService(String product) {
    if (downloadServicesByProduct.get(product) == null) {
      throw new ServiceNotAvailableException(
          "Download service not available for product " + product);
    }
    return downloadServicesByProduct.get(product);
  }

  public ApprovalService getApprovalService(String product) {
    if (approvalServicesByProduct.get(product) == null) {
      throw new ServiceNotAvailableException(
          "Approval service not available for product " + product);
    }
    return approvalServicesByProduct.get(product);
  }

  public ValidationService getValidationService(String product) {
    if(validationServicesByProduct.get(product) == null) {
      throw new ServiceNotAvailableException(
          "Validation service not available for product " + product);
    }
    return validationServicesByProduct.get(product);
  }

  public ConfigurationService getConfigurationService(String product) {
    if (configurationServicesByProduct.get(product) == null) {
      throw new ServiceNotAvailableException(
          "Configuration service not available for product " + product);
    }
    return configurationServicesByProduct.get(product);
  }
}
