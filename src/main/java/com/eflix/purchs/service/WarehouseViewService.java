package com.eflix.purchs.service;

import java.util.List;

import com.eflix.purchs.dto.WarehouseViewDTO;

public interface WarehouseViewService {
        // 창고 조회
    public List<WarehouseViewDTO> warehouseViewList();

    // 창고 조회 상세 (모달)
    public List<WarehouseViewDTO> warehouseViewListDetail(String warehouseId);
}
