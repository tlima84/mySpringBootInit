package com.example.unit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class ParserUtil {

    public static String asJsonString(Object request) throws JsonProcessingException {
        return new Jackson2ObjectMapperBuilder().build().writeValueAsString(request);
    }
}
