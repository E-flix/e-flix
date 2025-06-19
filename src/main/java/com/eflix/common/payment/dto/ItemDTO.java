package com.eflix.common.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
===============================================================
 작성자     : 복성민
 작성일     : 2025-06-19
 설명       : 결제 정보 데이터
---------------------------------------------------------------
 변경 이력 :
  - 2025-06-19 (복성민): 최초 생성
===============================================================
*/

@Data
@AllArgsConstructor
public class ItemDTO {
    private String id;
    private String name;
    private int price;
}
