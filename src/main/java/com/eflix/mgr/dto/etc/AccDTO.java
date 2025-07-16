package com.eflix.mgr.dto.etc;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccDTO {
    private List<Map<String, Object>> salaryTrend;
    private List<Map<String, Object>> pendingPayouts;
}
