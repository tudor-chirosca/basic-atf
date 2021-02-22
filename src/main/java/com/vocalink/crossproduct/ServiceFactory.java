package com.vocalink.crossproduct;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.domain.ResourceService;
import com.vocalink.crossproduct.domain.approval.ApprovalService;
import com.vocalink.crossproduct.domain.exception.ServiceNotAvailableException;
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

  private Map<String, ApprovalService> approvalServicesByProduct;
  private Map<String, ResourceService> downloadServicesByProduct;

  @PostConstruct
  public void init() {
    downloadServicesByProduct = resourceServices.stream()
        .collect(toMap(ResourceService::getProduct, Function.identity()));
    approvalServicesByProduct = approvalServices.stream()
        .collect(toMap(ApprovalService::getProduct, Function.identity()));
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
}
