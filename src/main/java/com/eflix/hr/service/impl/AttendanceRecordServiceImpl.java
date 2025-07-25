/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.etc.AttdDetailDTO;
import com.eflix.hr.dto.etc.AttdMgrListDTO;
import com.eflix.hr.dto.etc.AttdRecordDTO;
import com.eflix.hr.dto.etc.AttdRecordSummaryDTO;
import com.eflix.hr.dto.etc.AttdRemarkDTO;
import com.eflix.hr.dto.etc.AttdSearchDTO;
import com.eflix.hr.dto.etc.AttdSummaryDTO;
import com.eflix.hr.mapper.AttendanceRecordMapper;
import com.eflix.hr.service.AttendanceRecordService;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    @Autowired
    AttendanceRecordMapper attendanceRecordMapper;

    // 근태 전체조회
    @Override
    public List<AttendanceRecordDTO> getAllRecords() {
        return getAllRecords();
    }

    // 근태 상세조회
    @Override
    public List<AttendanceRecordDTO> getRecordsByEmpId(AttendanceRecordDTO attendanceRecordDTO) {
        attendanceRecordDTO.setEmpIdx(attendanceRecordDTO.getEmpIdx());
        return attendanceRecordMapper.getRecordsByEmpId(attendanceRecordDTO);
    }

    // 로그인 사원 근태현황 년월 드롭다운
    @Override
    public List<LocalDate> getYearMonthList(String coIdx, String empIdx) {
        return attendanceRecordMapper.getJoinDate(empIdx, coIdx);
    }

    // 근태관리 조회
    @Override
    public List<AttendanceRecordDTO> managerSearch(AttendanceRecordDTO attendanceRecordDTO) {
        return attendanceRecordMapper.managerSearch(attendanceRecordDTO);
    }

    @Override
    public List<AttendanceRecordDTO> getBasicInfo(AttendanceRecordDTO attendanceRecordDTO) {
        return attendanceRecordMapper.getBasicInfo(attendanceRecordDTO);
    }

    @Override
    public List<AttendanceRecordDTO> userInfo(AttendanceRecordDTO attendanceRecordDTO) {
        return attendanceRecordMapper.getBasicInfo(attendanceRecordDTO);
    }

    @Override
    public int createAttendanceRecord(AttendanceRecordDTO dto) {
        throw new UnsupportedOperationException("Unimplemented method 'createAttendanceRecord'");
    }

    @Override
    public int updateAttendanceRecord(AttendanceRecordDTO dto) {
        throw new UnsupportedOperationException("Unimplemented method 'updateAttendanceRecord'");
    }

    @Override
    public int deleteAttendanceRecord(String attdIdx) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteAttendanceRecord'");
    }

    // 0708
    @Override
    public AttdRecordSummaryDTO selectAttdRecordSummaryByEmpIdx(String empIdx, String date) {
        return attendanceRecordMapper.selectAttdRecordSummaryByEmpIdx(empIdx, date);
    }

    @Override
    public List<AttdRecordDTO> findAllByEmpIdxWithDate(String empIdx, String date) {
        return attendanceRecordMapper.findAllByEmpIdxWithDate(empIdx, date);
    }

    @Override
    public void addAttd() {
        attendanceRecordMapper.addAttd();
    }

    @Override
    public AttdSummaryDTO findAttdSummaryByEmpIdxWithDate(String empIdx, String date) {
        return attendanceRecordMapper.findAttdSummaryByEmpIdxWithDate(empIdx, date);
    }

    @Override
    public List<AttdDetailDTO> findAttdDetailListByEmpIdxWithDate(String empIdx, String date) {
        return attendanceRecordMapper.findAttdDetailListByEmpIdxWithDate(empIdx, date);
    }
    

    @Override
    public int findAllAttdCount(AttdSearchDTO attdSearchDTO) {
        return attendanceRecordMapper.findAllAttdCount(attdSearchDTO);
    }

    @Override
    public List<AttdMgrListDTO> findAttdMgrListByCoIdxWithDate(AttdSearchDTO attdSearchDTO) {
        return attendanceRecordMapper.findAttdMgrListByCoIdxWithDate(attdSearchDTO);
    }

    @Override
    public List<AttdRemarkDTO> findAttdRemarkListByEmpIdxWithDate(String empIdx, String date) {
        return attendanceRecordMapper.findAttdRemarkListByEmpIdxWithDate(empIdx, date);
    }

    @Override
    public List<AttendanceRecordDTO> findAllByEmpIdx(String empIdx) {
        return attendanceRecordMapper.findAllByEmpIdx(empIdx);
    }

    @Override
    public int isAlreadyCheckedIn(String empIdx) {
        return attendanceRecordMapper.isAlreadyCheckedIn(empIdx);
    }

    @Override
    public int insert(AttendanceRecordDTO attendanceRecordDTO) {
        return attendanceRecordMapper.insert(attendanceRecordDTO);
    }

    @Override
    public int isAlreadyCheckedOut(String empIdx) {
        return attendanceRecordMapper.isAlreadyCheckedOut(empIdx);
    }

    @Override
    public int update(AttendanceRecordDTO attendanceRecordDTO) {
        return attendanceRecordMapper.update(attendanceRecordDTO);
    }
}
