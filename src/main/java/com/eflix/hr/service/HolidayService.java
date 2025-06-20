/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 휴일 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.HolidayDTO;

public interface HolidayService {
    public List<HolidayDTO> getAllHolidays();
    public HolidayDTO getHolidayById(String hdIdx);
    int createHoliday(HolidayDTO dto);
    int updateHoliday(HolidayDTO dto);
    int deleteHoliday(String hdIdx);
}
