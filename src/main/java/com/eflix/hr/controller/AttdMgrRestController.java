package com.eflix.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.AttdMgrListDTO;
import com.eflix.hr.dto.etc.AttdSearchDTO;
import com.eflix.hr.service.AttendanceRecordService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/hr/attd/mgr")
public class AttdMgrRestController {
    
    @Autowired
    private AttendanceRecordService attendanceRecordService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @GetMapping("/list")
    public ResponseEntity<ResResult> getList(AttdSearchDTO attdSearchDTO) {
        ResResult result = null;

        attdSearchDTO.setCoIdx(getCoIdx());

        if(attdSearchDTO.getDateYear() == null && attdSearchDTO.getDateMonth() == null) {
            attdSearchDTO.setDate("2025-07");
        }

        int attdCount = attendanceRecordService.findAllAttdCount(attdSearchDTO);

        attdSearchDTO.setTotalRecord(attdCount);

        List<AttdMgrListDTO> emps = attendanceRecordService.findAttdMgrListByCoIdxWithDate(attdSearchDTO);

        if(emps != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("emps", emps);
            searchResult.put("total", attdCount);
            searchResult.put("page", attdSearchDTO.getPage());
            searchResult.put("startPage", attdSearchDTO.getStartPage());
            searchResult.put("endPage", attdSearchDTO.getEndPage());
            searchResult.put("lastPage", attdSearchDTO.getLastPage());

            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
