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

public final class LocalCensorServer extends AIServer {
    private static final URI SERVER_URI = URI.create("http://192.168.0.181:8001/api/chat");

    private static final String MODEL_NAME = "nemotron-cascade-2:30b";

    @Override
    public CompletableFuture<CensorResult> tryCensor(Censorable target) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = createPrompt(target);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(SERVER_URI)
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(MODEL_NAME, prompt, false, false)))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("HTTP POST request failed with response code " + response.statusCode());
                }

                JSONObject body = JSON.parseObject(response.body());
                JSONObject message = body.getJSONObject("message");
                String messageContent = message.getString("content");

                JSONObject resultJson = JSON.parseObject(messageContent.substring(messageContent.indexOf("{"), messageContent.lastIndexOf("}") + 1));

                String reason = resultJson.getString("reason");
                return resultJson.getBoolean("is_it_disease") ? CensorResult.bad(reason) : CensorResult.good(reason);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String createRequestBody(String model, String prompt, boolean think, boolean stream) {
        JSONObject json = new JSONObject();
        json.put("model", model);

        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        json.put("messages", messages);
        json.put("think", think);
        json.put("stream", stream);

        return json.toJSONString();
    }
}
