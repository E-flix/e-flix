/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 야간 근무 기록용 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NightRecordDTO {
    private String ntIdx;            // nt_idx
    private String empIdx;           // emp_idx
    private Date ntStart;   // nt_start
    private Date ntEnd;     // nt_end
    private Date ntDate;        // nt_date

    private String coIdx;

}
