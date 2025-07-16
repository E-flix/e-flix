package com.eflix.mgr.dto.etc;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchsDTO {
    private List<Map<String, Object>> inventoryFlow;
}
