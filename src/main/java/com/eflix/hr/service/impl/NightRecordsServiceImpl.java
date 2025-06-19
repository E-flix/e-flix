/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 야간 기록 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.NightRecordsDTO;
import com.eflix.hr.service.NightRecordsService;

@Service
public class NightRecordsServiceImpl implements NightRecordsService {

  @Override
  public List<NightRecordsDTO> getAllNightRecords() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllNightRecords'");
  }

  @Override
  public NightRecordsDTO getNightRecordById(String ntIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getNightRecordById'");
  }

  @Override
  public int createNightRecord(NightRecordsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createNightRecord'");
  }

  @Override
  public int updateNightRecord(NightRecordsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateNightRecord'");
  }

  @Override
  public int deleteNightRecord(String ntIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteNightRecord'");
  }

}
