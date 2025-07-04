package com.eflix.common.payment.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eflix.common.payment.dto.BillingReqDTO;
import com.eflix.common.payment.service.BillingService;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillingServiceImpl implements BillingService {
    
    @Value("${portone.v1.impCode}")
    private String impCode;

    @Value("${portone.v1.apiKey}")
    private String apiKey;

    @Value("${portone.v1.secretKey}")
    private String impV1SecretKey;

    @Value("${portone.channelKey}")
    private String impChannel;

    @Value("${portone.v2.secretKey}")
    private String impV2SecretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken() {
        Map<String, String> body = Map.of("imp_key", apiKey, "imp_secret", impV1SecretKey);

        ResponseEntity<Map> res = restTemplate.postForEntity(
                "https://api.iamport.kr/users/getToken",
                new HttpEntity<>(body),
                Map.class
        );
        Map<String, Object> response = (Map<String, Object>) res.getBody().get("response");
        return (String) response.get("access_token");
    }

    @Override
    public String verifyBillingKey(String customerUid) {
        String url = "https://api.portone.io/subscribe/customers/" + customerUid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + impV1SecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> body = (Map<String, Object>) response.getBody().get("billingKeyInfo");
        return body != null ? body.get("billingKey").toString() : "N/A";
    }

    @Override
    public String issueBillingKey(BillingReqDTO billingReqDTO) throws Exception {
        URL url = new URL("https://api.portone.io/billing-keys");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "PortOne " + impV2SecretKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // JSON 객체 구성 (Gson 기반)
        JsonObject credential = new JsonObject();
        credential.addProperty("number", billingReqDTO.getNumber());
        credential.addProperty("expiryYear", billingReqDTO.getExpiryYear());
        credential.addProperty("expiryMonth", billingReqDTO.getExpiryMonth());
        credential.addProperty("birthOrBusinessRegistrationNumber", billingReqDTO.getBirthOrBusinessRegistrationNumber());
        credential.addProperty("passwordTwoDigits", billingReqDTO.getPasswordTwoDigits());

        JsonObject card = new JsonObject();
        card.add("credential", credential);

        JsonObject method = new JsonObject();
        method.add("card", card);

        JsonObject customer = new JsonObject();
        customer.addProperty("id", billingReqDTO.getCustomerId());

        JsonObject body = new JsonObject();
        body.addProperty("channelKey", impChannel);
        body.add("customer", customer);
        body.add("method", method);

        String jsonBody = body.toString();
        log.info("보낼 JSON: {}", jsonBody);

        // 전송
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 응답
        int responseCode = conn.getResponseCode();
        log.info("응답 코드: {}", responseCode);

        InputStream is = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }

        br.close();
        conn.disconnect();

        log.info("응답 내용: {}", response);

        return response.toString();
    }
}
