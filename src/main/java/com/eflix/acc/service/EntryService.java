package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : EntryService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): 전표 전체조회 작성
  - 2025-06-24 (김희정): insert, update, delete 작성
=============================================== */
public interface EntryService {
	// 전표 전체조회
  public List<EntryMasterDTO> getList();
  // insert
  public EntryMasterDTO insertEntry(EntryMasterDTO entryMaster);
  // update
  public EntryMasterDTO updateEntry(EntryMasterDTO entryMaster);
  // delete
  public void deleteEntry(List<EntryDetailDTO> entryDetail);
  // select max+1 entry_number
  public int selectMaxPlusOneEntryNumber();
  // select max+1 line_number
  public int selectMaxPlusOneLineNumber(int entryNumber);
  // ========= 아래부터 매입매출전표 ==========
  // master 전표 조회 
  public List<EntryMasterDTO> getPSMasterList();
  // detail 전표 조회
  public List<EntryDetailDTO> getPSDetailList(int entryNumber);
  // 매입매출전표 신규 저장
  public EntryMasterDTO insertPurchaseSalesEntry(EntryMasterDTO entryMaster);
  // 매입매출전표 수정
  public EntryMasterDTO updatePurchaseSalesEntry(EntryMasterDTO entryMaster);
  // 매입매출전표용 전표번호 조회 (max+1)
  public int selectMaxPlusOneEntryNumberPS();
  // 매입매출전표 삭제
  public void deletePurchaseSalesEntry(List<EntryMasterDTO> entryMasters);
  // 매입매출전표 상세 저장
  public List<EntryDetailDTO> insertPurchaseSalesDetail(List<EntryDetailDTO> entryDetails);
  // 매입매출전표 상세 수정
  public List<EntryDetailDTO> updatePurchaseSalesDetail(List<EntryDetailDTO> entryDetails);
  // 매입매출전표 상세 삭제
  public void deletePurchaseSalesDetail(List<EntryDetailDTO> entryDetails);
}
