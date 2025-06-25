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
}
