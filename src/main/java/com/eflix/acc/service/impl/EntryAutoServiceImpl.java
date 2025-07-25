package com.eflix.acc.service.impl;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eflix.acc.dto.EntryAutoAllDTO;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.mapper.EntryAutoMapper;
import com.eflix.acc.service.EntryAutoService;
import com.eflix.common.security.auth.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryAutoServiceImpl implements EntryAutoService {

  private final EntryAutoMapper entryAutoMapper;

  public List<EntryAutoAllDTO> selectSalaryList() {
    return entryAutoMapper.selectSalaryList(AuthUtil.getCoIdx());
  }

  public Integer selectMaxPlusOneEntryNumber(EntryAutoAllDTO param) {
    param.setCoIdx(AuthUtil.getCoIdx());
    return entryAutoMapper.selectMaxPlusOneEntryNumber(param);
  }

  public Integer selectMaxPlusOneLineNumber(EntryAutoAllDTO param) {
    param.setCoIdx(AuthUtil.getCoIdx());
    return entryAutoMapper.selectMaxPlusOneLineNumber(param);
  }

  public EntryAutoAllDTO selectEntryMasterByRemarks(EntryAutoAllDTO param) {
    param.setCoIdx(AuthUtil.getCoIdx());
    return entryAutoMapper.selectEntryMasterByRemarks(param);
  }

  @Transactional
  public void insertSalaryEntryMaster(EntryMasterDTO params) {
    params.setCoIdx(AuthUtil.getCoIdx());
    entryAutoMapper.insertSalaryEntryMaster(params);
  }

  @Transactional
  public void insertEntryDetail(EntryDetailDTO detail) {
    detail.setCoIdx(AuthUtil.getCoIdx());
    entryAutoMapper.insertEntryDetail(detail);
  }

  @Transactional
  public void updateEntryDetail(EntryDetailDTO detail) {
    detail.setCoIdx(AuthUtil.getCoIdx());
    entryAutoMapper.updateEntryDetail(detail);
  }

  @Transactional
  public void updateEntryMaster(EntryMasterDTO master) {
    master.setCoIdx(AuthUtil.getCoIdx());
    entryAutoMapper.updateEntryMaster(master);
  }

  public List<EntryAutoAllDTO> selectAutoEntryAll(EntryAutoAllDTO param) {
    param.setCoIdx(AuthUtil.getCoIdx());
    return entryAutoMapper.selectAutoEntryAll(param);
  }

  @Override
  public void createSalaryEntryMasters() {
    List<EntryAutoAllDTO> salaryList = entryAutoMapper.selectSalaryList(AuthUtil.getCoIdx());
    for (EntryAutoAllDTO salary : salaryList) {
      String remarks = "인건비 " + salary.getAttMonth().substring(0,4) + "년 "
              + salary.getAttMonth().substring(5,7) + "월 귀속 "
              + salary.getPayMonth().substring(0,4) + "년 "
              + salary.getPayMonth().substring(5,7) + "월 "
              + salary.getPayMonth().substring(8,10) + "일 지급 자동전표";
      EntryAutoAllDTO check = new EntryAutoAllDTO(); // master 조회용
      check.setRemarks(remarks);
      check.setCoIdx(AuthUtil.getCoIdx());
      EntryAutoAllDTO exist = entryAutoMapper.selectEntryMasterByRemarks(check);
      if (exist == null) { // master가 없으면 생성
        int entryNumber = entryAutoMapper.selectMaxPlusOneEntryNumber(check); // 전표번호 자동생성
        EntryMasterDTO master = new EntryMasterDTO();
        master.setEntryNumber(entryNumber);
        master.setEntryDate(java.sql.Date.valueOf(salary.getPayMonth()));
        master.setCreator(AuthUtil.getEmpIdx());
        master.setCoIdx(AuthUtil.getCoIdx());
        master.setRemarks(remarks);
        entryAutoMapper.insertSalaryEntryMaster(master);
      }
    }
  }

  @Transactional
  public int insertBatch(List<EntryDetailDTO> entryDetailList) {
    int entryNumber = entryDetailList.get(0).getEntryNumber();
    Date entryDate = entryDetailList.get(0).getEntryDate();
    String coIdx = AuthUtil.getCoIdx();
    EntryAutoAllDTO check = new EntryAutoAllDTO();
    check.setCoIdx(coIdx);
    check.setEntryNumber(entryNumber);

    int successCount = 0;
    // 최초 1회만 max line number 조회
    int lineNumber = entryAutoMapper.selectMaxPlusOneLineNumber(check);

    System.out.println("insertBatch - entryDetailList: " + entryDetailList);
    for (EntryDetailDTO dto : entryDetailList) {
        dto.setEntryNumber(entryNumber);
        dto.setCoIdx(coIdx);
        dto.setLineNumber(lineNumber++); // 1씩 증가
        System.out.println("insertBatch - dto: " + dto);
        entryAutoMapper.insertEntryDetail(dto);
        successCount++;
    }
    EntryMasterDTO master = new EntryMasterDTO();
    master.setEntryNumber(entryNumber);
    master.setEntryDate(entryDate);
    master.setCoIdx(coIdx);
    entryAutoMapper.updateEntryMaster(master);
    return successCount;
  }

  @Override
  public List<EntryDetailDTO> selectEntryDetailList(int entryNumber, String coIdx) {
    return entryAutoMapper.selectEntryDetailList(entryNumber, coIdx);
  }
}
