package com.eflix.acc.service;

import java.util.List;

import com.eflix.acc.dto.EntryAutoAllDTO;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;

public interface EntryAutoService {
    List<EntryAutoAllDTO> selectSalaryList();
    Integer selectMaxPlusOneEntryNumber(EntryAutoAllDTO param);
    Integer selectMaxPlusOneLineNumber(EntryAutoAllDTO param);
    EntryAutoAllDTO selectEntryMasterByRemarks(EntryAutoAllDTO param);
    void insertSalaryEntryMaster(EntryMasterDTO params);
    void insertEntryDetail(EntryDetailDTO detail);
    void updateEntryDetail(EntryDetailDTO detail);
    void updateEntryMaster(EntryMasterDTO master);
    List<EntryAutoAllDTO> selectAutoEntryAll(EntryAutoAllDTO param);
    void createSalaryEntryMasters();
    int insertBatch(List<EntryDetailDTO> entryDetailList);
}
