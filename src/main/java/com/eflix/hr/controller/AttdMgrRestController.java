package com.eflix.hr.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.code.dto.CommonDTO;
import com.eflix.common.code.service.CommonService;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.etc.AttdMgrListDTO;
import com.eflix.hr.dto.etc.AttdRemarkDTO;
import com.eflix.hr.dto.etc.AttdSearchDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.GradeService;
import com.eflix.main.service.CompanyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/hr/attd/mgr")
public class AttdMgrRestController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private CommonService commonService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @GetMapping("/year")
    public ResponseEntity<ResResult> getYear() {
        ResResult result = null;

        Date date = companyService.findCoRegdateByCoIdx(getCoIdx());
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int regYear = localDate.getYear();
        int nowYear = LocalDate.now().getYear();

        List<String> yearList = new ArrayList<>();

        for (int i = regYear; i <= nowYear; i++) {
            yearList.add(String.valueOf(i));
        }

        result = ResUtil.makeResult(ResStatus.OK, yearList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/month")
    public ResponseEntity<ResResult> getMonth(@RequestParam String year) {
        ResResult result = null;

        Date date = companyService.findCoRegdateByCoIdx(getCoIdx());
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int regYear = localDate.getYear();
        int nowYear = LocalDate.now().getYear();

        List<String> monthList = new ArrayList<>();
        if (Integer.parseInt(year) == nowYear) {
            for (int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
                monthList.add(String.valueOf(i));
            }
        } else if (regYear == nowYear) {
            for (int i = regYear; i <= 12; i++) {
                monthList.add(String.valueOf(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                monthList.add(String.valueOf(i));
            }
        }

        result = ResUtil.makeResult(ResStatus.OK, monthList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/searchOptions")
    public ResponseEntity<ResResult> getMethodName() {
        ResResult result = null;

        List<DepartmentDTO> depts = departmentService.findUpAllByCoIdx(getCoIdx());
        List<GradeDTO> grades = gradeService.findAllByCoIdx(getCoIdx());
        List<CommonDTO> status = commonService.getCommon("AR");

        if ((depts != null) || (grades != null) || (status != null)) {
            Map<String, Object> options = new HashMap<>();
            options.put("depts", depts);
            options.put("grades", grades);
            options.put("status", status);

            result = ResUtil.makeResult(ResStatus.OK, options);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ResResult> getList(AttdSearchDTO attdSearchDTO) {
        ResResult result = null;

        attdSearchDTO.setCoIdx(getCoIdx());

        String year = attdSearchDTO.getDateYear();
        String month = attdSearchDTO.getDateMonth();

        if (Stream.of(year, month).anyMatch(v -> v == null || v.isEmpty() || "-".equals(v))) {
            attdSearchDTO.setDate(null);
        } else {
            attdSearchDTO.setDate(year + "-" + month);
        }

        int attdCount = attendanceRecordService.findAllAttdCount(attdSearchDTO);

        attdSearchDTO.setTotalRecord(attdCount);

        List<AttdMgrListDTO> emps = attendanceRecordService.findAttdMgrListByCoIdxWithDate(attdSearchDTO);

        if (emps != null) {
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

    @GetMapping("/detail")
    public ResponseEntity<ResResult> getDetail(@RequestParam String empIdx, @RequestParam String year,
            @RequestParam String month) {
        ResResult result = null;

        // String date = year + "-" + month;
        String date = "2025-06";

        List<AttdRemarkDTO> attdRemarkDTO = attendanceRecordService.findAttdRemarkListByEmpIdxWithDate(empIdx, date);

        if (attdRemarkDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdRemarkDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
