/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 연차 신청 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.LeaveRequestsDTO;
import com.eflix.hr.service.LeaveRequestsService;

@Service
public class LeaveRequestsServiceImpl implements LeaveRequestsService {

  @Override
  public List<LeaveRequestsDTO> getAllLeaveRequests() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllLeaveRequests'");
  }

  @Override
  public LeaveRequestsDTO getLeaveRequestById(String leaveReqIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getLeaveRequestById'");
  }

  @Override
  public int createLeaveRequest(LeaveRequestsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createLeaveRequest'");
  }

  @Override
  public int updateLeaveRequest(LeaveRequestsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateLeaveRequest'");
  }

  @Override
  public int deleteLeaveRequest(String leaveReqIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteLeaveRequest'");
  }

}
