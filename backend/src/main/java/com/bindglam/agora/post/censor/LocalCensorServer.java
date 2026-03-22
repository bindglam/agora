package com.bindglam.agora.post.censor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.bindglam.agora.post.PostDTO;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class LocalCensorServer implements CensorServer {
    private static final String MODEL_NAME = "qwen3.5:35b-a3b";
    private static final String PROMPT_TEMPLATE =
                    """
                    당신은 따뜻하고 공정하며, 교육적인 관점을 가진 '학급 게시판 관리 도우미'입니다. 제공된 게시물의 제목과 내용을 참고하여 이 게시물이 학급 가이드라인에 적합한지 검토하고, 부적절한 부분이 있는지를 판별하십시오.
                    
                    [필수 분석 지침]
                    1. **비속어 및 혐오 표현**: 욕설, 비하 발언, 차별적 언어(성별, 지역, 외모 등)가 포함되었는지 엄격하게 확인하십시오.
                    2. **사생활 침해 및 저격**: 특정 친구의 실명을 거론하며 비난하거나, 허락받지 않은 개인 정보(전화번호, 사진 등)를 노출했는지를 확인하십시오.
                    3. **갈등 조장**: 반 분위기를 해치거나 편을 가르는 공격적인 말투가 포함되었는지 검토하십시오.
                    4. **부적절한 주제**: 도박, 유해 매체, 불법적인 활동에 관한 내용인지 검토하십시오.
                    5. 응답은 반드시 다음 순서를 따르십시오:
                       - 그 다음, 아래 JSON 형식을 엄격히 지켜서 ```json ... ``` 코드 블록 안에 최종 출력하십시오.
                    
                    {
                      “is_it_disease“: 검열이 필요할시 true, 아니면 false,
                      "reason": "근거"
                    }
                    
                    ---
                    
                    제목: {post_title}
                    내용:
                    {post_content}
                    """;

    @Override
    public CompletableFuture<CensorResult> tryCensor(PostDTO post) {
        return CompletableFuture.supplyAsync(() -> {
            String prompt = PROMPT_TEMPLATE.replace("{post_title}", post.getTitle()).replace("{post_content}", post.getContent());

            try(HttpClient client = HttpClient.newBuilder().build()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://192.168.0.181:8001/api/chat"))
                        .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(MODEL_NAME, prompt, false, false)))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
