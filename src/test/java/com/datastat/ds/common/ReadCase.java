package com.datastat.ds.common;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadCase {
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    public static JsonNode readFile(String filePath) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(new File(filePath));
        return jsonNode;
    }

    public static JsonNode getCase(String field, JsonNode file) {
        return file.get(field);
    }
}
