package com.eflix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.eflix.main.mapper.SubscriptionMapper;

@SpringBootTest
public class SubscriptionTest {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Test
    void 테스트() {
        subscriptionMapper.findActiveSubscriptionByCoIdx("spi-101");
    }

}
