package com.bindglam.agora.school.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bindglam.agora.school.*;
import com.bindglam.agora.school.repository.PerformanceAssessmentRepository;
import com.bindglam.agora.user.DefaultUserDTO;
import com.bindglam.agora.user.User;
import com.bindglam.agora.user.UserPermission;
import com.bindglam.agora.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
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

    private final PerformanceAssessmentRepository performanceAssessmentRepository;

    private final UserService userService;

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

    @Override
    public @NonNull CompletableFuture<@NonNull MealMenu> getMealMenu(@NonNull LocalDate date) {
        return CompletableFuture.supplyAsync(() -> {
            List<MealMenu.Meal> meals = new ArrayList<>();

            String MLSV_YMD = date.format(DATE_TIME_FORMATTER); // 급식일자

            URI uri = URI.create(API_BASE_URL + "/mealServiceDietInfo?KEY=" + API_KEY + "&Type=json&ATPT_OFCDC_SC_CODE=" + ATPT_OFCDC_SC_CODE + "&SD_SCHUL_CODE=" + SD_SCHUL_CODE
                    + "&MLSV_YMD=" + MLSV_YMD + "&MLSV_FROM_YMD=" + MLSV_YMD + "&MLSV_TO_YMD=" + MLSV_YMD);

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

                JSONObject mealMenuRaw = body.getJSONArray("mealServiceDietInfo").getJSONObject(1).getJSONArray("row").getJSONObject(0);
                String[] mealsRaw = mealMenuRaw.getString("DDISH_NM").split("<br/>");
                for(String mealRaw : mealsRaw) {
                    meals.add(
                            MealMenu.Meal.builder()
                                    .name(mealRaw)
                                    .build()
                    );
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }

            return new MealMenu(meals);
        });
    }

    @Override
    public @NonNull List<PerformanceAssessmentDTO> getPerformanceAssessments(@NonNull LocalDate date) {
        return performanceAssessmentRepository.findAllByEvaluateAt(date).stream().map(PerformanceAssessment::toDTO).toList();
    }

    @Override
    public SchoolRequests.PerformanceAssessment.Create.@NonNull Response createPerformanceAssessment(SchoolRequests.PerformanceAssessment.@NonNull Create request) {
        DefaultUserDTO userDTO = userService.getUnsafeUser(UUID.fromString(request.uploaderKey())).orElse(null);
        if(userDTO == null)
            return SchoolRequests.PerformanceAssessment.Create.Response.builder().errorMessage("알 수 없는 유저입니다.").build();
        User user = userDTO.toEntity();

        if(!user.getPermission().check(UserPermission.ADMIN))
            return SchoolRequests.PerformanceAssessment.Create.Response.builder().errorMessage("권한이 없습니다.").build();

        PerformanceAssessment performanceAssessment = PerformanceAssessment.builder()
                .subject(request.subject())
                .title(request.title())
                .description(request.description())
                .evaluateAt(request.evaluateAt())
                .build();

        performanceAssessment = performanceAssessmentRepository.save(performanceAssessment);

        return SchoolRequests.PerformanceAssessment.Create.Response.builder()
                .performanceAssessment(performanceAssessment.toDTO())
                .build();
    }
}
