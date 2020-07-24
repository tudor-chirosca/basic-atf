package com.vocalink.crossproduct.ui.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresenterFactory {
  private final List<Presenter> participantClientList;

  private Map<ClientType, Presenter> presenterByClientType = new HashMap<>();

  @PostConstruct
  public void init() {
    presenterByClientType = participantClientList.stream()
        .collect(Collectors.toMap(Presenter::getClientType, Function.identity()));
  }

  public Presenter getPresenterForClient(ClientType clientType){
    if (presenterByClientType.get(clientType) == null) {
      throw new RuntimeException("Presenter not available for client type " + clientType);
    }
    return presenterByClientType.get(clientType);
  }
}
