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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.LeaveRequestDTO;
import com.eflix.hr.dto.etc.LeaveRequestAppDTO;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaReqSummaryDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.dto.etc.VaSummaryDTO;
import com.eflix.hr.mapper.LeaveRequestMapper;
import com.eflix.hr.service.LeaveRequestService;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Override
    public int insert(VaDTO vaDTO) {
        return leaveRequestMapper.insert(vaDTO);
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return null;
    }

    @Override
    public LeaveRequestDTO getLeaveRequestById(String leaveReqIdx) {
        return null;
    }

    @Override
    public int createLeaveRequest(LeaveRequestDTO dto) {
        return 0;
    }

    @Override
    public int updateLeaveRequest(LeaveRequestDTO dto) {
        return 0;
    }

    @Override
    public int deleteLeaveRequest(String leaveReqIdx) {
        return 0;
    }

    @Override
    public VaSummaryDTO findSummaryByEmpIdxWithCoIdx(String empIdx, String coIdx) {
        return leaveRequestMapper.findSummaryByEmpIdxWithCoIdx(empIdx, coIdx);
    }

    @Override
    public List<VaDTO> findAllByEmpIdxWithCoIdx(String empIdx, String coIdx) {
        return leaveRequestMapper.findAllByEmpIdxWithCoIdx(empIdx, coIdx);
    }

    @Override
    public VaReqSummaryDTO findReqSummaryBySearch(VaSearchDTO vaSearchDTO) {
        return leaveRequestMapper.findReqSummaryBySearch(vaSearchDTO);
    }

    @Override
    public int findCountBySearch(VaSearchDTO vaSearchDTO) {
        return leaveRequestMapper.findCountBySearch(vaSearchDTO);
    }

    @Override
    public List<VaDTO> findAllBySearch(VaSearchDTO vaSearchDTO) {
        return leaveRequestMapper.findAllBySearch(vaSearchDTO);
    }

    @Override
    public VaDTO findByLeaveReqIdx(String leaveReqIdx) {
        return leaveRequestMapper.findByLeaveReqIdx(leaveReqIdx);
    }

    @Override
    public int insertReqApprover(LeaveRequestAppDTO leaveRequestAppDTO) {
        return leaveRequestMapper.insertReqApprover(leaveRequestAppDTO);
    }

    @Override
    public int insertReqApprover(List<LeaveRequestAppDTO> list) {
        int affectedRows = 0;
        for(LeaveRequestAppDTO dto: list) {
            leaveRequestMapper.insertReqApprover(dto);
            affectedRows++;
        }
        return affectedRows;
    }
}
