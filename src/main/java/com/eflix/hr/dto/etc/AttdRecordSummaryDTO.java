package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class AttdRecordSummaryDTO {
    private int realDay;
    private int totalLeaveDay;
    private int workDay;
    private int usedLeaveDay;
    private int canLeaveDay;
    private int abDay;
    private int rateCount;
    private int leaveEarlyDay;
    private int holidayWorkDay;
}
