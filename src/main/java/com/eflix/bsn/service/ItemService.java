// src/main/java/com/eflix/bsn/service/ItemService.java
package com.eflix.bsn.service;

import java.util.List;
import com.eflix.bsn.dto.BsnItemDTO;

public interface ItemService {
    /** 모든 품목 조회 */
    List<BsnItemDTO> getAllItems();

    /** 코드로 단건 조회 */
    BsnItemDTO getItemByCode(String itemCode);

    /** 이름으로 검색 조회 */
    List<BsnItemDTO> searchItemsByName(String itemName);
}
