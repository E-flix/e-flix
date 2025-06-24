package com.eflix.acc.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.mapper.EntryMapper;
import com.eflix.acc.service.EntryService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : entryServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): 전표 전체조회 작성
  - 2025-06-24 (김희정): insert, update, delete 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
  private final EntryMapper entryMapper;

  /**
   * 전표 전체조회
   */
  @Override
  public List<EntryMasterDTO> getList() {
    return entryMapper.getList();
  }

  // insert
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void insertEntry(EntryMasterDTO entryMaster) {
    entryMapper.insertEntryMaster(entryMaster); // master insert
    if (entryMaster.getDetails() != null) { // detail 테이블 insert위해서 추출
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setEntryNumber(entryMaster.getEntryNumber()); // FK 설정 => master.entry == detail.entry
        entryMapper.insertEntryDetail(detail); // detail insert
      }
    }
  }

  // update
  @Override
  @Transactional(rollbackFor = Exception.class) 
  public void updateEntry(EntryMasterDTO entryMaster) {
    entryMapper.updateEntryMaster(entryMaster); // master update
    // 기존 상세는 모두 삭제 후 다시 삽입
    entryMapper.deleteEntryDetailsByEntryNumber(entryMaster.getEntryNumber()); // detail delete
    if (entryMaster.getDetails() != null) {
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setEntryNumber(entryMaster.getEntryNumber()); // FK 설정 => master.entry == detail.entry
        entryMapper.insertEntryDetail(detail); // detail insert
      }
    }
  }

  // delete
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteEntry(List<EntryDetailDTO> entryDetail) {
    if (entryDetail == null || entryDetail.isEmpty()) return;
    for (EntryDetailDTO entry : entryDetail){
      entryMapper.deleteEntryDetailsByLineNumber(entry); // detail delete
      // master delete => master는 detail count<1 이면 삭제
      int enNo = entryDetail.get(0).getEntryNumber(); // EntryNumber 추출
      int count = entryMapper.selectCountDetailByEntryNumber(enNo); // 반복 시점 detail 갯수
      if (count < 1) {
        entryMapper.deleteEntryMasterByEntryNumber(enNo);
      }
    }
  }

  // select max+1 entry_number
  @Override
  public int selectMaxPlusOneEntryNumber() {
    return entryMapper.selectMaxPlusOneEntryNumber();
  }

  // select max+1 line_number
  @Override
  public int selectMaxPlusOneLineNumber(int entryNumber) {
    return entryMapper.selectMaxPlusOneLineNumber(entryNumber);
  }
}