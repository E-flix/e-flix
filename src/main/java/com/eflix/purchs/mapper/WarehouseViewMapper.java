package com.eflix.purchs.mapper;

import java.util.List;

import com.eflix.purchs.dto.WarehouseViewDTO;

public interface WarehouseViewMapper {
    // 창고 조회
    public List<WarehouseViewDTO> warehouseViewList(WarehouseViewDTO warehouse);

    // 창고 조회 상세 (모달)
    public List<WarehouseViewDTO> warehouseViewListDetail(String warehouseId);

    // 창고 위치 드롭다운
    public List<WarehouseViewDTO> searchWarehouseLocation();


}
