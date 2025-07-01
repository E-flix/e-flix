/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 휴일 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.HolidayDTO;
import com.eflix.hr.service.HolidayService;

@Service
public class HolidayServiceImpl implements HolidayService{

  @Override
  public List<HolidayDTO> getAllHolidays() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllHolidays'");
  }

  @Override
  public HolidayDTO getHolidayById(String hdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getHolidayById'");
  }

  @Override
  public int createHoliday(HolidayDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createHoliday'");
  }

  @Override
  public int updateHoliday(HolidayDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateHoliday'");
  }

  @Override
  public int deleteHoliday(String hdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteHoliday'");
  }

}
