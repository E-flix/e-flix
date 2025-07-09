package com.eflix.acc.service.impl;

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

    public List<EntryAutoAllDTO> selectSalaryList(EntryAutoAllDTO param) {
        param.setCoIdx(AuthUtil.getCoIdx());
        return entryAutoMapper.selectSalaryList(param);
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
        entryAutoMapper.insertSalaryEntryMaster(params);
    }

    @Transactional
    public void insertEntryDetail(EntryDetailDTO detail) {
        entryAutoMapper.insertEntryDetail(detail);
    }

    @Transactional
    public void updateEntryDetail(EntryDetailDTO detail) {
        entryAutoMapper.updateEntryDetail(detail);
    }

    @Transactional
    public void updateEntryMaster(EntryMasterDTO master) {
        entryAutoMapper.updateEntryMaster(master);
    }

    public List<EntryAutoAllDTO> selectAutoEntryAll(EntryAutoAllDTO param) {
        param.setCoIdx(AuthUtil.getCoIdx());
        return entryAutoMapper.selectAutoEntryAll(param);
    }
}
