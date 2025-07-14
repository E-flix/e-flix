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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.AttendanceRequestDTO;
import com.eflix.hr.mapper.AttendanceRequestMapper;
import com.eflix.hr.service.AttendanceRequestService;

@Service
public class AttendanceRequestServiceImpl implements AttendanceRequestService{

  @Autowired
  private AttendanceRequestMapper attendanceRequestMapper;

  @Override
  public List<AttendanceRequestDTO> getAllAttendanceRequests() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllAttendanceRequests'");
  }

  @Override
  public AttendanceRequestDTO getAttendanceRequestById(String editIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getAttendanceRequestById'");
  }

  @Override
  public int createAttendanceRequest(AttendanceRequestDTO dto) {

    throw new UnsupportedOperationException("Unimplemented method 'createAttendanceRequest'");
  }

  @Override
  public int updateAttendanceRequest(AttendanceRequestDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateAttendanceRequest'");
  }

  @Override
  public int deleteAttendanceRequest(String editIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAttendanceRequest'");
  }

  //근태 신청 POST
  @Override
  public int insert(AttendanceRequestDTO attendanceRequestDTO) {
    return attendanceRequestMapper.insert(attendanceRequestDTO);
  }
  
  // 근태 신청 승인(관리자)
  @Override
  public int update(AttendanceRequestDTO attendanceRequestDTO) {
    return attendanceRequestMapper.update(attendanceRequestDTO);
  }

}
