package com.eflix.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eflix.common.security.interceptor.UserTypeAccessInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
}
