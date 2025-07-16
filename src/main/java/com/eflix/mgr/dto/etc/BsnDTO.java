package com.eflix.mgr.dto.etc;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BsnDTO {
    private List<Map<String, Object>> salesTrend;
    private List<Map<String, Object>> weeklyOrderStats;
}
