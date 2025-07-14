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
import com.eflix.hr.dto.etc.LeaveRequestAppDTO;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaReqSummaryDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.dto.etc.VaSummaryDTO;

public interface LeaveRequestService {
    public List<LeaveRequestDTO> getAllLeaveRequests();
    public LeaveRequestDTO getLeaveRequestById(String leaveReqIdx);
    int createLeaveRequest(LeaveRequestDTO dto);
    int updateLeaveRequest(LeaveRequestDTO dto);
    int deleteLeaveRequest(String leaveReqIdx);

    // 0711
    public int insert(VaDTO vaDTO);
    public VaSummaryDTO findSummaryByEmpIdxWithCoIdx(String empIdx, String coIdx);
    public List<VaDTO> findAllByEmpIdxWithCoIdx(String empIdx, String coIdx);
    public VaReqSummaryDTO findReqSummaryBySearch(VaSearchDTO vaSearchDTO);
    public int findCountBySearch(VaSearchDTO vaSearchDTO);
    public List<VaDTO> findAllBySearch(VaSearchDTO vaSearchDTO);
    public VaDTO findByLeaveReqIdx(String leaveReqIdx);

    // 0712
    public int insertReqApprover(LeaveRequestAppDTO leaveRequestAppDTO);
    public int insertReqApprover(List<LeaveRequestAppDTO> list);
}
