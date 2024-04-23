package com.datastat.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import lombok.SneakyThrows;

public class ModerationUtil {

    public static String getHuaweiCloudToken(String body, String username, String password, String domain, String endpoint) {
        try {
            body = String.format(body, username, password, domain);
            HttpResponse<com.mashape.unirest.http.JsonNode> response = Unirest
                    .post(endpoint)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();

            String token = response.getHeaders().get("X-Subject-Token").get(0);
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    @SneakyThrows
    public static boolean moderation(String url, String text, String token) {
        String body = String.format("{\"items\":[{\"text\":\"%s\",\"type\":\"content\"}]}", text);
        HttpResponse<String> response = Unirest.post(url)
                    .header("X-Auth-Token", token)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode resp = objectMapper.readTree(response.getBody());
        if (response.getStatus() == 200 && resp.get("result").get("suggestion").asText().equals("pass"))
            return true;
        return false;
    }
}
