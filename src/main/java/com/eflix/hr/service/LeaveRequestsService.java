/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 연차 신청 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.LeaveRequestsDTO;

public interface LeaveRequestsService {
    public List<LeaveRequestsDTO> getAllLeaveRequests();
    public LeaveRequestsDTO getLeaveRequestById(String leaveReqIdx);
    int createLeaveRequest(LeaveRequestsDTO dto);
    int updateLeaveRequest(LeaveRequestsDTO dto);
    int deleteLeaveRequest(String leaveReqIdx);
}
