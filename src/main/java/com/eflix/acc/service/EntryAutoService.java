package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.dto.EntryAutoAllDTO;

public interface EntryAutoService {
    List<EntryAutoAllDTO> selectSalaryList(EntryAutoAllDTO param);
    Integer selectMaxPlusOneEntryNumber(EntryAutoAllDTO param);
    Integer selectMaxPlusOneLineNumber(EntryAutoAllDTO param);
    EntryAutoAllDTO selectEntryMasterByRemarks(EntryAutoAllDTO param);
    void insertSalaryEntryMaster(EntryMasterDTO params);
    void insertEntryDetail(EntryDetailDTO detail);
    void updateEntryDetail(EntryDetailDTO detail);
    void updateEntryMaster(EntryMasterDTO master);
    List<EntryAutoAllDTO> selectAutoEntryAll(EntryAutoAllDTO param);
}
