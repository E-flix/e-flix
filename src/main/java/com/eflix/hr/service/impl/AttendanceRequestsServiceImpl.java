/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 요청 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.AttendanceRequestsDTO;
import com.eflix.hr.service.AttendanceRequestsService;

@Service
public class AttendanceRequestsServiceImpl implements AttendanceRequestsService{

  @Override
  public List<AttendanceRequestsDTO> getAllAttendanceRequests() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllAttendanceRequests'");
  }

  @Override
  public AttendanceRequestsDTO getAttendanceRequestById(String editIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAttendanceRequestById'");
  }

  @Override
  public int createAttendanceRequest(AttendanceRequestsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createAttendanceRequest'");
  }

  @Override
  public int updateAttendanceRequest(AttendanceRequestsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateAttendanceRequest'");
  }

  @Override
  public int deleteAttendanceRequest(String editIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteAttendanceRequest'");
  }

}
