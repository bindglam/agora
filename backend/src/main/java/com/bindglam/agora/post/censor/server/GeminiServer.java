package com.bindglam.agora.post.censor.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.bindglam.agora.post.censor.CensorResult;
import com.bindglam.agora.post.censor.Censorable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public final class GeminiServer extends AIServer {
    private static final String SERVER_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent";

    private static final String MODEL_NAME = "gemini-2.5-flash";
    private static final String API_KEY = "AIzaSyC1Sh4NhGf-KtkE7_HIJML0D7w7XElTzH8";

    @Override
    public CompletableFuture<CensorResult> tryCensor(Censorable target) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = createPrompt(target);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_BASE_URL.replace("{model}", MODEL_NAME)))
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(prompt)))
                    .header("x-goog-api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .build();

            try {
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("HTTP POST request failed with response code " + response.statusCode());
                }

                JSONObject body = JSON.parseObject(response.body());
                JSONArray candidates = body.getJSONArray("candidates");
                JSONObject candidate = candidates.getJSONObject(0);
                JSONObject content = candidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                JSONObject part = parts.getJSONObject(0);
                String text = part.getString("text");

                JSONObject resultJson = JSON.parseObject(text.substring(text.indexOf("{"), text.lastIndexOf("}") + 1));

                String reason = resultJson.getString("reason");
                return resultJson.getBoolean("is_it_disease") ? CensorResult.bad(reason) : CensorResult.good(reason);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String createRequestBody(String prompt) {
        JSONObject json = new JSONObject();

        JSONArray contents = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        json.put("contents", contents);

        return json.toJSONString();
    }
}
