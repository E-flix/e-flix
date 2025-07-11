/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 연차 신청 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.LeaveRequestDTO;
import com.eflix.hr.dto.etc.VaDTO;
import com.eflix.hr.dto.etc.VaSearchDTO;
import com.eflix.hr.dto.etc.VaSummaryDTO;

public interface LeaveRequestMapper {
    public List<LeaveRequestDTO> selectAll();
    public LeaveRequestDTO selectById(@Param("leaveReqIdx") String leaveReqIdx);
    int update(LeaveRequestDTO dto);
    int deleteById(@Param("leaveReqIdx") String leaveReqIdx);

    public int insert(VaDTO vaDTO);
    public VaSummaryDTO findSummaryByEmpIdxWithCoIdx(String empIdx, String coIdx);
    public List<VaDTO> findAllByEmpIdxWithCoIdx(String empIdx, String coIdx);
    public List<VaDTO> findAllBySearch(VaSearchDTO vaSearchDTO);
}
