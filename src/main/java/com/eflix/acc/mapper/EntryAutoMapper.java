package com.eflix.acc.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eflix.acc.dto.EntryAutoAllDTO;
import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;

@Mapper
public interface EntryAutoMapper {
    List<EntryAutoAllDTO> selectSalaryList(String coIdx);
    Integer selectMaxPlusOneEntryNumber(EntryAutoAllDTO param);
    Integer selectMaxPlusOneLineNumber(EntryAutoAllDTO param);
    EntryAutoAllDTO selectEntryMasterByRemarks(EntryAutoAllDTO param);
    void insertSalaryEntryMaster(EntryMasterDTO params);
    int insertEntryDetail(EntryDetailDTO detail);
    void updateEntryDetail(EntryDetailDTO detail);
    void updateEntryMaster(EntryMasterDTO master);
    List<EntryAutoAllDTO> selectAutoEntryAll(EntryAutoAllDTO param);
    List<EntryDetailDTO> selectEntryDetailList(@Param("entryNumber") int entryNumber, @Param("coIdx") String coIdx);
}
