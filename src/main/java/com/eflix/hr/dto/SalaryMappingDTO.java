/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 매핑 관리용 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryMappingDTO {
    private String mpIdx;            // mp_idx
    private String alNumber;         // al_number
    private String salaryType;       // salary_type
    private String alName;           // al_name
    private String type;             // type

    private String coIdx;

}
