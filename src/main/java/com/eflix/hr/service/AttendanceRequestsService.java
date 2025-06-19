/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 요청 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.AttendanceRequestsDTO;

public interface AttendanceRequestsService {
    public List<AttendanceRequestsDTO> getAllAttendanceRequests();
    public AttendanceRequestsDTO getAttendanceRequestById(String editIdx);
    int createAttendanceRequest(AttendanceRequestsDTO dto);
    int updateAttendanceRequest(AttendanceRequestsDTO dto);
    int deleteAttendanceRequest(String editIdx);
}
