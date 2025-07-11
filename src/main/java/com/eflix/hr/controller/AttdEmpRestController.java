package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.AttendanceRequestDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.etc.AttdDetailDTO;
import com.eflix.hr.dto.etc.AttdRecordDTO;
import com.eflix.hr.dto.etc.AttdRecordSummaryDTO;
import com.eflix.hr.dto.etc.AttdSummaryDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.AttendanceRequestService;
import com.eflix.hr.service.EmployeeService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/hr/attd/emp")
public class AttdEmpRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private AttendanceRequestService attdReqService;

    @Value("${upload.path}")
    private String path;

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

        AttdSummaryDTO attdSummaryDTO = attendanceRecordService.findAttdSummaryByEmpIdxWithDate(getEmpIdx(), date);

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

        List<AttdDetailDTO> attdDetailDTOs = attendanceRecordService.findAttdDetailListByEmpIdxWithDate(getEmpIdx(), date);

        if(attdDetailDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdDetailDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/req")
    public ResponseEntity<ResResult> postMethodName(
        @RequestPart("reqData") AttendanceRequestDTO attendanceRequestDTO,
        @RequestPart(value = "reqFile", required = false) MultipartFile reqFile) throws IllegalStateException, IOException {
        ResResult result = null;

        if (reqFile != null) {
            String uploadDir = path + "/hr/attd/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(uploadDir + reqFile.getOriginalFilename());
            reqFile.transferTo(dest);

            attendanceRequestDTO.setReqFile(reqFile.getOriginalFilename());
        }

        attendanceRequestDTO.setEmpIdx(getEmpIdx());

        int affectedRows = attdReqService.insert(attendanceRequestDTO);

        if (affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "등록 과정에서 오류가 발생했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
