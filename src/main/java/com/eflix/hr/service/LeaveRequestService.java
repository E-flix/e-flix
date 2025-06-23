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

import com.eflix.hr.dto.LeaveRequestDTO;

public interface LeaveRequestService {
    public List<LeaveRequestDTO> getAllLeaveRequests();
    public LeaveRequestDTO getLeaveRequestById(String leaveReqIdx);
    int createLeaveRequest(LeaveRequestDTO dto);
    int updateLeaveRequest(LeaveRequestDTO dto);
    int deleteLeaveRequest(String leaveReqIdx);
}
