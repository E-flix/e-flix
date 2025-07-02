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
import com.eflix.common.security.auth.AuthContext;
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
  private final AuthContext authContext;

  /**
   * 전표 전체조회
   */
  @Override
  public List<EntryMasterDTO> getList() {
    String coIdx = authContext.getCoIdx(); // 회사 인덱스 추출
    return entryMapper.getList(coIdx);
  }

  // insert
  @Override
  @Transactional(rollbackFor = Exception.class)
  public EntryMasterDTO insertEntry(EntryMasterDTO entryMaster) {
    entryMaster.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    Date now = new Date(); // 현재 시각
    EntryDetailDTO detailDTO = new EntryDetailDTO(); // detail DTO 생성
    detailDTO.setCoIdx(authContext.getCoIdx());
    detailDTO.setEntryNumber(entryMaster.getEntryNumber());
    // detail Count가 1이상이라면 이미 master가 있다는 뜻이므로 master insert는 생략
    int count = entryMapper.selectCountDetailByEntryNumber(detailDTO); 
    if (count == 0) entryMapper.insertEntryMaster(entryMaster); // master update
    if (entryMaster.getDetails() != null) { // detail 테이블 insert위해서 추출
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
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
    entryMaster.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    EntryDetailDTO detailDTO = new EntryDetailDTO(); // detail DTO 생성
    detailDTO.setCoIdx(authContext.getCoIdx());
    detailDTO.setEntryNumber(entryMaster.getEntryNumber());
    entryMapper.updateEntryMaster(entryMaster); // master insert
    // 기존 상세는 모두 삭제 후 다시 삽입
    entryMapper.deleteEntryDetailsByEntryNumber(detailDTO); // detail delete
    if (entryMaster.getDetails() != null) {
      for (EntryDetailDTO detail : entryMaster.getDetails()) {
        detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
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
        detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
        entryMapper.deleteEntryDetailsByLineNumber(detail);  // detail 삭제
        entryNumbers.add(detail.getEntryNumber());           // 중복 없이 entryNumber 저장
    }
    for (Integer enNo : entryNumbers) {
        EntryDetailDTO detailDTO = new EntryDetailDTO();
        detailDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정 
        detailDTO.setEntryNumber(enNo);             // entryNumber 설정
        EntryMasterDTO masterDTO = new EntryMasterDTO();
        masterDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
        masterDTO.setEntryNumber(enNo);             // entryNumber 설정
        int count = entryMapper.selectCountDetailByEntryNumber(detailDTO);  // detail 개수 확인
        if (count < 1) {
            entryMapper.deleteEntryMasterByEntryNumber(masterDTO);          // detail이 없으면 master 삭제
        }
    }
  }

  // select max+1 entry_number
  @Override
  public int selectMaxPlusOneEntryNumber() {
    String coIdx = authContext.getCoIdx(); // 회사 인덱스 추출
    return entryMapper.selectMaxPlusOneEntryNumber(coIdx);
  }

  // select max+1 line_number
  @Override
  public int selectMaxPlusOneLineNumber(int entryNumber) {
    EntryDetailDTO detailDTO = new EntryDetailDTO();
    detailDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    detailDTO.setEntryNumber(entryNumber);      // entryNumber 설정
    return entryMapper.selectMaxPlusOneLineNumber(detailDTO);
  }

  // ========= 아래부터 매입매출전표 ==========
  // master 전표 조회
  @Override
  public List<EntryMasterDTO> getPSMasterList() {
    String coIdx = authContext.getCoIdx(); // 회사 인덱스 추출
    return entryMapper.getPSMasterList(coIdx);
  }
  // detail 전표 조회
  @Override
  public List<EntryDetailDTO> getPSDetailList(int entryNumber) {
    EntryDetailDTO detailDTO = new EntryDetailDTO();
    detailDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    detailDTO.setEntryNumber(entryNumber);      // entryNumber 설정
    return entryMapper.getPSDetailList(detailDTO);
  }

  // 매입매출전표 신규 저장
  @Override
  @Transactional(rollbackFor = Exception.class)
  public EntryMasterDTO insertPurchaseSalesEntry(EntryMasterDTO entryMaster) {
    Date now = new Date();
    entryMaster.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    // 매입매출전표 master 저장
    if (entryMaster.getCreatedAt() == null) {
      entryMaster.setCreatedAt(now);
    }
    if (entryMaster.getUpdatedAt() == null) {
      entryMaster.setUpdatedAt(now);
    }
    // 전표번호가 없으면 매입매출전표용 전표번호로 새로 발번
    if (entryMaster.getEntryNumber() == 0) {
      entryMaster.setEntryNumber(selectMaxPlusOneEntryNumberPS());
    }
    entryMapper.insertEntryMaster(entryMaster);
    // 매입매출전표에는 상세 데이터가 없으므로 master만 저장
    return entryMaster;
  }

  // 매입매출전표 수정
  @Override
  @Transactional(rollbackFor = Exception.class)
  public EntryMasterDTO updatePurchaseSalesEntry(EntryMasterDTO entryMaster) {
    Date now = new Date();
    entryMaster.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
    if (entryMaster.getUpdatedAt() == null) {
      entryMaster.setUpdatedAt(now);
    }
    entryMapper.updateEntryMaster(entryMaster);
    return entryMaster;
  }

  // 매입매출전표용 전표번호 조회 (max+1)
  @Override
  public int selectMaxPlusOneEntryNumberPS() {
    String coIdx = authContext.getCoIdx(); // 회사 인덱스 추출
    return entryMapper.selectMaxPlusOneEntryNumberPS(coIdx);
  }

  // 매입매출전표 마스터 삭제
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deletePurchaseSalesEntry(List<EntryMasterDTO> entryMasters) {
    if (entryMasters == null || entryMasters.isEmpty()) return;
    for (EntryMasterDTO master : entryMasters) {
      // 마스터를 삭제하기 전에 관련된 모든 상세 데이터(분개)를 삭제합니다.
      EntryDetailDTO detailDTO = new EntryDetailDTO();
      detailDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
      detailDTO.setEntryNumber(master.getEntryNumber()); // entryNumber 설정
      entryMapper.deleteEntryDetailsByEntryNumber(detailDTO);
      // 매입매출전표 마스터를 삭제합니다.
      EntryMasterDTO masterDTO = new EntryMasterDTO();
      masterDTO.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
      masterDTO.setEntryNumber(master.getEntryNumber()); // entryNumber 설정
      entryMapper.deleteEntryMasterByEntryNumber(masterDTO);
    }
  }

  // 매입매출전표 상세 삭제
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deletePurchaseSalesDetail(List<EntryDetailDTO> entryDetails) {
    if (entryDetails == null || entryDetails.isEmpty()) return;
    
    for (EntryDetailDTO detail : entryDetails) {
      detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
      entryMapper.deleteEntryDetailsByLineNumber(detail);
    }
  }

  // 매입매출전표 상세 저장
  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<EntryDetailDTO> insertPurchaseSalesDetail(List<EntryDetailDTO> entryDetails) {
    if (entryDetails == null || entryDetails.isEmpty()) return entryDetails;
    
    Date now = new Date();
    
    for (EntryDetailDTO detail : entryDetails) {
      // 라인번호가 없으면 새로 발번
      if (detail.getLineNumber() == 0) {
        detail.setLineNumber(selectMaxPlusOneLineNumber(detail.getEntryNumber()));
      }
      
      // 생성/수정 시간 설정
      if (detail.getCreatedAt() == null) {
        detail.setCreatedAt(now);
      }
      if (detail.getUpdatedAt() == null) {
        detail.setUpdatedAt(now);
      }
      detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
      entryMapper.insertEntryDetail(detail);
    }
    
    return entryDetails;
  }

  // 매입매출전표 상세 수정
  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<EntryDetailDTO> updatePurchaseSalesDetail(List<EntryDetailDTO> entryDetails) {
    if (entryDetails == null || entryDetails.isEmpty()) return entryDetails;
    
    Date now = new Date();
    
    for (EntryDetailDTO detail : entryDetails) {
      // 수정 시간 설정
      if (detail.getUpdatedAt() == null) {
        detail.setUpdatedAt(now);
      }
      detail.setCoIdx(authContext.getCoIdx()); // 회사 인덱스 설정
      entryMapper.updateEntryDetail(detail);
    }
    
    return entryDetails;
  }
}