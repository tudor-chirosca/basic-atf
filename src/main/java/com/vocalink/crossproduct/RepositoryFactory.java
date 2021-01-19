package com.vocalink.crossproduct;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.domain.alert.AlertRepository;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.exception.RepositoryNotAvailableException;
import com.vocalink.crossproduct.domain.files.FileRepository;
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import com.vocalink.crossproduct.domain.reference.ReferencesRepository;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.domain.transaction.TransactionRepository;
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
  private final List<ReferencesRepository> referencesRepository;
  private final List<BatchRepository> batchClientList;
  private final List<FileRepository> fileClientList;
  private final List<SettlementsRepository> settlementsRepositories;
  private final List<AlertRepository> alertClientList;
  private final List<ParticipantIODataRepository> participantIODataRepositories;
  private final List<TransactionRepository> transactionClientList;

  private Map<String, ParticipantRepository> participantRepositoriesByProduct;
  private Map<String, CycleRepository> cycleRepositoriesByProduct;
  private Map<String, PositionRepository> positionRepositoriesByProduct;
  private Map<String, IntraDayPositionGrossRepository> intraDayPositionGrossRepositoriesByProduct;
  private Map<String, ReferencesRepository> referencesRepositoriesByProduct;
  private Map<String, BatchRepository> batchRepositoriesByProduct;
  private Map<String, FileRepository> fileRepositoriesByProduct;
  private Map<String, SettlementsRepository> settlementsRepositoriesByProduct;
  private Map<String, AlertRepository> alertRepositoriesByProduct;
  private Map<String, ParticipantIODataRepository> participantIODataRepositoriesByProduct;
  private Map<String, TransactionRepository> transactionRepositoriesByProduct;

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
    referencesRepositoriesByProduct = referencesRepository.stream()
        .collect(toMap(ReferencesRepository::getProduct, Function.identity()));
    batchRepositoriesByProduct = batchClientList.stream()
        .collect(toMap(BatchRepository::getProduct, Function.identity()));
    fileRepositoriesByProduct = fileClientList.stream()
        .collect(toMap(FileRepository::getProduct, Function.identity()));
    settlementsRepositoriesByProduct = settlementsRepositories.stream()
        .collect(toMap(SettlementsRepository::getProduct, Function.identity()));
    alertRepositoriesByProduct = alertClientList.stream()
        .collect(toMap(AlertRepository::getProduct, Function.identity()));
    participantIODataRepositoriesByProduct = participantIODataRepositories.stream()
        .collect(toMap(ParticipantIODataRepository::getProduct, Function.identity()));
    transactionRepositoriesByProduct = transactionClientList.stream()
        .collect(toMap(TransactionRepository::getProduct, Function.identity()));
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

  public ReferencesRepository getReferencesRepository(String product) {
    if (referencesRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "References Repository not available for product " + product);
    }
    return referencesRepositoriesByProduct.get(product);
  }

  public BatchRepository getBatchRepository(String product) {
    if (batchRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Batch repository not available for product " + product);
    }
    return batchRepositoriesByProduct.get(product);
  }

  public FileRepository getFileRepository(String product) {
    if (fileRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Files repository not available for context " + product);
    }
    return fileRepositoriesByProduct.get(product);
  }

  public SettlementsRepository getSettlementsRepository(String product) {
    if (settlementsRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException("Settlement repository not available for context " + product);
    }
    return settlementsRepositoriesByProduct.get(product);
  }

  public AlertRepository getAlertsRepository(String product) {
    if (alertRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Alerts repository not available for product " + product);
    }
    return alertRepositoriesByProduct.get(product);
  }

  public ParticipantIODataRepository getParticipantsIODataRepository(String product) {
    if (participantIODataRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException(
          "Participants IO Data repository not available for product " + product);
    }
    return participantIODataRepositoriesByProduct.get(product);
  }

  public TransactionRepository getTransactionRepository(String product) {
    if (transactionRepositoriesByProduct.get(product) == null) {
      throw new RepositoryNotAvailableException("Transaction repository not available for product " + product);
    }
    return transactionRepositoriesByProduct.get(product);
  }
}
