package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.ReferencesRepository;
import com.vocalink.crossproduct.shared.reference.ReferencesClient;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReferencesRepositoryAdapter implements ReferencesRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<MessageDirectionReference> getMessageDirectionReferences(String context) {
    log.info("Fetching all messages direction references from context {} ... ", context);

    ReferencesClient client = clientFactory.getReferencesClient(context.toUpperCase());

    return client.findMessageDirectionReferences()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }
}
