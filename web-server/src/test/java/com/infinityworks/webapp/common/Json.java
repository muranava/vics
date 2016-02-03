package com.infinityworks.webapp.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Json {
    public static ObjectMapper objectMapper = new ObjectMapper()
            .registerModules(new JavaTimeModule(), new Jdk8Module())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
}
