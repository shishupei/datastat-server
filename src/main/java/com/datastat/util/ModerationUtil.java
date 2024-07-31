package com.datastat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import lombok.SneakyThrows;

public class ModerationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ModerationUtil.class);

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
        if (text == null) {
            return true;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode itemNode = objectMapper.createObjectNode();
        ArrayNode itemsNode = objectMapper.createArrayNode();
        itemNode.put("text", text);
        itemNode.put("type", "content");
        itemsNode.add(itemNode);

        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.set("items", itemsNode);
        String body = objectMapper.writeValueAsString(bodyNode);

        HttpResponse<String> response = Unirest.post(url)
                .header("X-Auth-Token", token)
                .header("Content-Type", "application/json")
                .body(body)
                .asString();
        JsonNode resp = objectMapper.readTree(response.getBody());
        if (response.getStatus() == 200 && resp.get("result").get("suggestion").asText().equals("pass"))
            return true;
        logger.info(response.getBody());
        return false;
    }
}
