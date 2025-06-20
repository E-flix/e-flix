package com.eflix.common.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "portone.secret")
public class PortOneSecretProperties {
    private String api;
    private String webHook;
}
