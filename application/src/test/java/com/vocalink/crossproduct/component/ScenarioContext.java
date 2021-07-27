package com.vocalink.crossproduct.component;

import com.vocalink.crossproduct.component.exceptions.ResourceNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;

@Component
@Profile("component")
public class ScenarioContext {

    private final HashMap<Class, Object> scenarioResources = new HashMap<>();

    private final LinkedList<RequestEntity<?>> requestList = new LinkedList<>();

    private final LinkedList<ResponseEntity<?>> responseList = new LinkedList<>();

    public <T> T getScenarioResource(Class<T> clazz) throws ResourceNotFoundException {
        if (!scenarioResources.containsKey(clazz)) {
            throw new ResourceNotFoundException();
        }
        return (T) scenarioResources.get(clazz);
    }

    public <T> void putScenarioResource(Class<T> clazz, Object obj) {
        if (scenarioResources.containsKey(clazz)) {
            scenarioResources.replace(clazz, obj);
        } else {
            scenarioResources.put(clazz, obj);
        }
    }

    public void clearState() {
        requestList.clear();
        responseList.clear();
        scenarioResources.clear();
    }

    public RequestEntity<?> getLastRequest() {
        return requestList.getLast();
    }

    public ResponseEntity<?> getLastResponse() {
        return responseList.getLast();
    }

    public void addRequest(RequestEntity<?> request) {
        requestList.add(request);
    }

    public void addResponse(ResponseEntity<?> response) {
        responseList.add(response);
    }
}
