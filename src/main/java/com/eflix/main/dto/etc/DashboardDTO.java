package com.eflix.main.dto.etc;

import lombok.Data;

@Data
public class DashboardDTO {
    private int subscriberCount;
    private int subscriptionRequestCount;
    private int unansweredCount;
    private int todayAnsweredCount;
}
