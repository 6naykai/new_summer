package com.nbu.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonUtil {
    final private ObjectMapper objectMapper = new ObjectMapper();

    public String ObjectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object JsonStringToObject(String json,Class objectClassInfo){
        try {
            Reader jsonString = new StringReader(json);
            return objectMapper.readValue(jsonString,objectClassInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
