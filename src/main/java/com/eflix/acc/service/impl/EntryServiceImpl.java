package com.eflix.acc.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  public EntryMasterDTO insertEntry(EntryMasterDTO entryMaster) {
    Date now = new Date(); // 현재 시각
    
    // detail Count가 1이상이라면 이미 master가 있다는 뜻이므로 master insert는 생략
    int count = entryMapper.selectCountDetailByEntryNumber(entryMaster.getEntryNumber()); 
    if (count == 0) entryMapper.insertEntryMaster(entryMaster); // master update
    if (entryMaster.getDetails() != null) { // detail 테이블 insert위해서 추출
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setEntryNumber(entryMaster.getEntryNumber()); // FK 설정 => master.entry == detail.entry
        // created_at, updated_at 필드가 null인 경우 현재 시각으로 설정
        if (detail.getCreatedAt() == null) {
          detail.setCreatedAt(now);
        }
        if (detail.getUpdatedAt() == null) {
          detail.setUpdatedAt(now);
        }
        entryMapper.insertEntryDetail(detail); // detail insert
      }
    }
    return entryMaster;
  }

  // update
  @Override
  @Transactional(rollbackFor = Exception.class) 
  public EntryMasterDTO updateEntry(EntryMasterDTO entryMaster) {
    Date now = new Date(); // 현재 시각
    
    entryMapper.updateEntryMaster(entryMaster); // master insert
    // 기존 상세는 모두 삭제 후 다시 삽입
    entryMapper.deleteEntryDetailsByEntryNumber(entryMaster.getEntryNumber()); // detail delete
    if (entryMaster.getDetails() != null) {
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setEntryNumber(entryMaster.getEntryNumber()); // FK 설정 => master.entry == detail.entry
        // created_at, updated_at 필드가 null인 경우 현재 시각으로 설정
        if (detail.getCreatedAt() == null) {
          detail.setCreatedAt(now);
        }
        if (detail.getUpdatedAt() == null) {
          detail.setUpdatedAt(now);
        }
        entryMapper.insertEntryDetail(detail); // detail insert
      }
    }
    return entryMaster;
  }

  // delete
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteEntry(List<EntryDetailDTO> entryDetail) {
    if (entryDetail == null || entryDetail.isEmpty()) return;
    // 중복 없이 EntryNumber를 저장할 Set 생성
    Set<Integer> entryNumbers = new HashSet<>();
    for (EntryDetailDTO detail : entryDetail) {
        entryMapper.deleteEntryDetailsByLineNumber(detail);  // detail 삭제
        entryNumbers.add(detail.getEntryNumber());           // 중복 없이 entryNumber 저장
    }
    for (Integer enNo : entryNumbers) {
        int count = entryMapper.selectCountDetailByEntryNumber(enNo);  // detail 개수 확인
        if (count < 1) {
            entryMapper.deleteEntryMasterByEntryNumber(enNo);          // detail이 없으면 master 삭제
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

  // ========= 아래부터 매입매출전표 ==========
  // master 전표 조회
  @Override
  public List<EntryMasterDTO> getPSMasterList() {
    return entryMapper.getPSMasterList();
  }
  // detail 전표 조회
  @Override
  public List<EntryDetailDTO> getPSDetailList(int entryNumber) {
    return entryMapper.getPSDetailList(entryNumber);
  }
}