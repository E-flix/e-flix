package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.AttdDetailDTO;
import com.eflix.hr.dto.etc.AttdRecordDTO;
import com.eflix.hr.dto.etc.AttdRecordSummaryDTO;
import com.eflix.hr.dto.etc.AttdSummaryDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.EmployeeService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/hr/attd/emp")
public class AttdEmpRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    private String getEmpIdx() {
        return AuthUtil.getEmpIdx();
    }

    @GetMapping("/year")
    public ResponseEntity<ResResult> getYear() {
        ResResult result = null;

        Date date = employeeService.findAllEmpRegdateByEmpIdx(getEmpIdx());
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

        Date date = employeeService.findAllEmpRegdateByEmpIdx(getEmpIdx());
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int regYear = localDate.getYear();
        int nowYear = LocalDate.now().getYear();

        List<String> monthList = new ArrayList<>();
        if(Integer.parseInt(year) == nowYear) {
            for(int i = 1; i <= LocalDate.now().getMonthValue(); i++) {
                monthList.add(String.valueOf(i));
            } 
        }
        else if (regYear == nowYear) {
            for (int i = regYear; i <= 12 ; i++) {
                monthList.add(String.valueOf(i));
            }
        } else {
            for (int i = 1;  i <= 12; i++) {
                monthList.add(String.valueOf(i));
            }
        }

        result = ResUtil.makeResult(ResStatus.OK, monthList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<ResResult> getSummary(@RequestParam String date) {
        ResResult result = null;

        System.out.println(date);

        // AttdRecordSummaryDTO attdRecordSummaryDTO = attendanceRecordService.selectAttdRecordSummaryByEmpIdx(getEmpIdx(), date);
        AttdSummaryDTO attdSummaryDTO = attendanceRecordService.selectAttdSummary(getEmpIdx(), date);

        if(attdSummaryDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdSummaryDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ResResult> getList(@RequestParam String date) {
        ResResult result = null;

        // List<AttdRecordDTO> attd = attendanceRecordService.findAllByEmpIdxWithDate(getEmpIdx(), date);
        List<AttdDetailDTO> attdDetailDTOs = attendanceRecordService.selectAttdDetailList(getEmpIdx(), date);

        if(attdDetailDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdDetailDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
