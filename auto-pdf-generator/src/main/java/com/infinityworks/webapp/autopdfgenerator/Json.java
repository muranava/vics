package com.infinityworks.webapp.autopdfgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

public class Json {
    static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new GuavaModule());
}
