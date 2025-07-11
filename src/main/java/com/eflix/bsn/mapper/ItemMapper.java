// src/main/java/com/eflix/bsn/mapper/ItemMapper.java
package com.eflix.bsn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.common.payment.dto.ItemDTO;

@Mapper
public interface ItemMapper {
    /** 모든 품목 조회 */
    List<BsnItemDTO> selectAllItems();

    /** 품목코드로 단일 품목 조회 */
    BsnItemDTO selectItemByCode(@Param("itemCode") String itemCode);

    /** 품목명으로 검색 (부분 일치) */
    List<BsnItemDTO> selectItemsByName(@Param("itemName") String itemName);

    List<ItemDTO> selectAll();
    List<ItemDTO> selectByName(@Param("name") String name);
}
