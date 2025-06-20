/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 야간 기록 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.NightRecordsDTO;

public interface NightRecordsService {
    public List<NightRecordsDTO> getAllNightRecords();
    public NightRecordsDTO getNightRecordById(String ntIdx);
    int createNightRecord(NightRecordsDTO dto);
    int updateNightRecord(NightRecordsDTO dto);
    int deleteNightRecord(String ntIdx);
}