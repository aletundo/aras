package it.unimib.disco.aras.analysesconfiguratorservice.entity;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonConverter implements AttributeConverter<Map<String, String>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert to database column value to JSON");
            throw new RuntimeException("Could not convert to Json", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            log.error("Failed to convert to entity attribute from JSON");
            throw new RuntimeException("Could not convert from Json", e);
        }
    }

}
