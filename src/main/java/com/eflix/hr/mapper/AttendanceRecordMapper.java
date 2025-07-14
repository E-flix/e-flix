/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.etc.AttdDetailDTO;
import com.eflix.hr.dto.etc.AttdMgrListDTO;
import com.eflix.hr.dto.etc.AttdRecordDTO;
import com.eflix.hr.dto.etc.AttdRecordSummaryDTO;
import com.eflix.hr.dto.etc.AttdRemarkDTO;
import com.eflix.hr.dto.etc.AttdSearchDTO;
import com.eflix.hr.dto.etc.AttdSummaryDTO;

@Mapper
public interface AttendanceRecordMapper {

    // 근태 전체조회
    List<AttendanceRecordDTO> getAllRecords();

    // 근태 상세조회
    List<AttendanceRecordDTO> getRecordsByEmpId(AttendanceRecordDTO dto);

    // 로그인 사원 근태현황 년월 드롭다운
    List<LocalDate> getJoinDate(String empIdx, String coIdx);

    // 로그인 사원 근태현황 기본정보
    List<AttendanceRecordDTO> getBasicInfo(AttendanceRecordDTO dto);

    // 근태관리 조회
    List<AttendanceRecordDTO> managerSearch(AttendanceRecordDTO dto);

    // 0708
    public AttdRecordSummaryDTO selectAttdRecordSummaryByEmpIdx(String empIdx, String date);

    public List<AttdRecordDTO> findAllByEmpIdxWithDate(String empIdx, String date);

    public void addAttd();

    AttdSummaryDTO findAttdSummaryByEmpIdxWithDate(String empIdx, String date);

    List<AttdDetailDTO> findAttdDetailListByEmpIdxWithDate(String empIdx, String date);

    List<AttdRemarkDTO> findAttdRemarkList(String empIdx, String date);

    int findAllAttdCount(AttdSearchDTO attdSearchDTO);

    List<AttdMgrListDTO> findAttdMgrListByCoIdxWithDate(AttdSearchDTO attdSearchDTO);

    List<AttdRemarkDTO> findAttdRemarkListByEmpIdxWithDate(String empIdx, String date);

    // 0710
    List<AttendanceRecordDTO> findAllByEmpIdx(String empIdx);

    // 0714
    public int isAlreadyCheckedIn(String empIdx);
    public int insert(AttendanceRecordDTO attendanceRecordDTO);
    public int isAlreadyCheckedOut(String empIdx);
    public int update(AttendanceRecordDTO attendanceRecordDTO);
}
