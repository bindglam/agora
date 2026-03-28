package com.bindglam.agora.school.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.bindglam.agora.school.Timetable;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public final class SchoolServiceImpl implements SchoolService {
    private static final String API_BASE_URL = "https://open.neis.go.kr/hub";
    private static final String API_KEY = "299ee3b4aca148c38ccf14cf1a334a6f";

    private static final String ATPT_OFCDC_SC_CODE = "J10"; // 시도교육청코드
    private static final String SD_SCHUL_CODE = "7569054"; // 행정표준코드
    private static final String GRADE = "3"; // 학년
    private static final String CLASS_NM = "2"; // 반

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Override
    public @NonNull CompletableFuture<@NonNull Timetable> getTimetable(@NonNull LocalDate date) {
        return CompletableFuture.supplyAsync(() -> {
            List<Timetable.Time> times = new ArrayList<>();

            String AY = String.valueOf(date.getYear()); // 학년도
            String ALL_TI_YMD = date.format(DATE_TIME_FORMATTER); // 시간표일자

            URI uri = URI.create(API_BASE_URL + "/misTimetable?KEY=" + API_KEY + "&Type=json&ATPT_OFCDC_SC_CODE=" + ATPT_OFCDC_SC_CODE + "&SD_SCHUL_CODE=" + SD_SCHUL_CODE + "&GRADE=" + GRADE + "&CLASS_NM=" + CLASS_NM
                    + "&AY=" + AY+ "&ALL_TI_YMD=" + ALL_TI_YMD);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            try {
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("HTTP GET request failed with response code " + response.statusCode());
                }

                JSONObject body = JSON.parseObject(response.body());

                List<JSONObject> timesRaw = body.getJSONArray("misTimetable").getJSONObject(1).getJSONArray("row").toList(JSONObject.class);
                for(JSONObject timeRaw : timesRaw) {
                    times.add(
                            Timetable.Time.builder()
                                    .position(Integer.parseInt(timeRaw.getString("PERIO")))
                                    .name(timeRaw.getString("ITRT_CNTNT"))
                                    .build()
                    );
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

            Collections.sort(times);

            return new Timetable(times);
        });
    }
}
