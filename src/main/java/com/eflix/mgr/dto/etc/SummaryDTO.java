package com.eflix.mgr.dto.etc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDTO {
    private int onDutyCount;
    private int tardyCount;
    // private double thisMonthSales;
    private int inventoryCount;
    private int stockShortageCount;
    private int todayProcessCount;
}
