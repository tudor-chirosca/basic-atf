package com.vocalink.crossproduct.component.util;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vocalink.crossproduct.component.exceptions.TestExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class JsonUtil {

    private static final ObjectMapper OM = new ObjectMapper();

    static {
        OM.enable(SerializationFeature.INDENT_OUTPUT);
        OM.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        OM.registerModule(new JavaTimeModule());
        OM.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public String toJson(Object obj) {
        try {
            return OM.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Unable to create request body. Object=%s", obj), e);
        }
    }

    public <T> T fromJson(String obj, Class<T> clazz) {
        try {
            return OM.readValue(obj, clazz);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to create object of type %s from json %s", clazz, obj), e);
        }
    }

    public <T> Collection<T> fromJsonCollection(String obj, Class<T> clazz) {
        try {
            JavaType type = OM.getTypeFactory().constructCollectionType(List.class, clazz);
            return OM.readValue(obj, type);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to create collection. Object=%s", obj), e);
        }
    }

    public JsonNode asJsonNode(String data) {
        try {
            return OM.readValue(data, ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to convert string to json object. String=%s", data), e);
        }
    }

    public boolean areEqual(String s1, String s2) {
        try {
            return OM.readTree(s1).equals(OM.readTree(s2));
        } catch (Exception ex) {
            log.debug("Comparison failed: " + ex.getMessage());
            return false;
        }
    }

    public String makePretty(String s) throws Exception {
        try {
            return OM.readTree(s).toPrettyString();
        } catch (Exception ex){
            log.debug("Beautify failed for string: " + s);
            throw new TestExecutionException("Beautify failed for string: " + s);
        }
    }
}
