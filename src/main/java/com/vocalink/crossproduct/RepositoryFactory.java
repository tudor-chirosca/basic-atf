package com.vocalink.crossproduct;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.exception.RepositoryNotAvailableException;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepositoryFactory {

  private final List<ParticipantRepository> participantRepositories;
  private final List<CycleRepository> cycleRepositories;
  private final List<PositionRepository> positionRepositories;
  private final List<IntraDayPositionGrossRepository> intraDayPositionGrossRepositories;

  private Map<String, ParticipantRepository> participantRepositoriesByProduct;
  private Map<String, CycleRepository> cycleRepositoriesByProduct;
  private Map<String, PositionRepository> positionRepositoriesByProduct;
  private Map<String, IntraDayPositionGrossRepository> intraDayPositionGrossRepositoriesByProduct;

  @PostConstruct
  public void init() {
    participantRepositoriesByProduct = participantRepositories.stream()
        .collect(toMap(ParticipantRepository::getProduct, Function.identity()));
    cycleRepositoriesByProduct = cycleRepositories.stream()
        .collect(toMap(CycleRepository::getProduct, Function.identity()));
    positionRepositoriesByProduct = positionRepositories.stream()
        .collect(toMap(PositionRepository::getProduct, Function.identity()));
    intraDayPositionGrossRepositoriesByProduct = intraDayPositionGrossRepositories.stream()
        .collect(toMap(IntraDayPositionGrossRepository::getProduct, Function.identity()));
  }

  public ParticipantRepository getParticipantRepository(String product) {
    if (participantRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Participant repository not available for product " + product);
    }
    return participantRepositoriesByProduct.get(product);
  }

  public CycleRepository getCycleRepository(String product) {
    if (cycleRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Cycle repository not available for product " + product);
    }
    return cycleRepositoriesByProduct.get(product);
  }

  public PositionRepository getPositionRepository(String product) {
    if (positionRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Position repository not available for product " + product);
    }
    return positionRepositoriesByProduct.get(product);
  }

  public IntraDayPositionGrossRepository getIntradayPositionGrossRepository(String product) {
    if (intraDayPositionGrossRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Intraday position gross repository not available for product " + product);
    }
    return intraDayPositionGrossRepositoriesByProduct.get(product);
  }
}
