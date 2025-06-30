package com.eflix.acc.mapper;

import java.util.List;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : EntryMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): 전표 전체조회 작성
  - 2025-06-24 (김희정): insert, update, delete 작성
=============================================== */
public interface EntryMapper {
  // 전표 전체조회
  public List<EntryMasterDTO> getList();

  // insert
  public int insertEntryMaster(EntryMasterDTO entryMaster);
  public int insertEntryDetail(EntryDetailDTO entryDetail);

  // update
  public int updateEntryMaster(EntryMasterDTO entryMaster);
  public int updateEntryDetail(EntryDetailDTO entryDetail);

  // delete
  public int deleteEntryDetailsByLineNumber(EntryDetailDTO entryDetail);
  public int deleteEntryDetailsByEntryNumber(int entryNumber);
  public int deleteEntryMasterByEntryNumber(int entryNumber);
  // EntryDetail 갯수 => master 삭제할때 Detail < count 1이면 삭제해야 함
  public int selectCountDetailByEntryNumber(int entryNumber);

  // select max+1 entry_number
  public int selectMaxPlusOneEntryNumber();
  // select max+1 line_number
  public int selectMaxPlusOneLineNumber(int entryNumber);

  // ========= 아래부터 매입매출전표 ==========
  // master 전표 조회
  public List<EntryMasterDTO> getPSMasterList();
  // detail 전표 조회 
  public List<EntryDetailDTO> getPSDetailList(int entryNumber);
  // 매입매출전표용 전표번호 조회 (max+1)
  public int selectMaxPlusOneEntryNumberPS();
}
