package com.eflix.common.payment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.webhook.WebhookVerifier;

@Configuration
public class PortOneConfig {

    @Autowired
    private PortOneSecretProperties secretProperties;

    @Bean
    public PaymentClient paymentClient() {
        System.out.println(secretProperties.getApi() + secretProperties.getStoreId());
        return new PaymentClient(secretProperties.getApi(), "https://api.portone.io", secretProperties.getStoreId());
    }

    @Bean
    public WebhookVerifier webhookVerifier() {
        // 해당 메서드에서 오류 날 시 아래 행 주석처리
        return new WebhookVerifier(secretProperties.getWebHook());
        // return null;
    }
}