package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.infrastructure.exception.PresenterNotAvailableForClientTypeException;
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

  private Map<ClientType, Presenter> presenterByUserAgent = new HashMap<>();

  @PostConstruct
  public void init() {
    presenterByUserAgent = participantClientList.stream()
        .collect(Collectors.toMap(Presenter::getClientType, Function.identity()));
  }

  public Presenter getPresenter(ClientType clientType) {
    if (presenterByUserAgent.get(clientType) == null) {
      throw new PresenterNotAvailableForClientTypeException("Presenter not available for client type " + clientType);
    }
    return presenterByUserAgent.get(clientType);
  }
}
