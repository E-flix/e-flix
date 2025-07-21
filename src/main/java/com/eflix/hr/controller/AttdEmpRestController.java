package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.etc.AttdDetailDTO;
import com.eflix.hr.dto.etc.AttdSummaryDTO;
import com.eflix.hr.dto.etc.CompanyIpDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.AttendanceRequestService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.service.CompanyService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/hr/attd/emp")
public class AttdEmpRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private AttendanceRequestService attdReqService;

    @Autowired
    private CompanyService companyService;

    @Value("${upload.path}")
    private String path;

    private String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    private String getEmpIdx() {
        return AuthUtil.getEmpIdx();
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // IPv6 localhost 대응
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    public boolean isCompanyTrustedIp(String coIdx, String clientIp) {
        List<CompanyIpDTO> whitelist = companyService.findWhiteListByCoIdx(coIdx);

        for (CompanyIpDTO ipRule : whitelist) {
            if ("PREFIX".equalsIgnoreCase(ipRule.getIpType())) {
                if (clientIp.startsWith(ipRule.getIpValue())) {
                    return true;
                }
            } else if ("EXACT".equalsIgnoreCase(ipRule.getIpType())) {
                if (clientIp.equals(ipRule.getIpValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/in")
    public ResponseEntity<ResResult> in(HttpServletRequest request) {
        ResResult result = null;

        String ip = getClientIp(request);
        AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();
        if (!isCompanyTrustedIp(getCoIdx(), ip)) {
            result = ResUtil.makeResult("400", "사내 IP내에서만 출근이 가능합니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        int isAlreadyCheckedIn = attendanceRecordService.isAlreadyCheckedIn(getEmpIdx());

        if (isAlreadyCheckedIn > 0) {
            result = ResUtil.makeResult("400", "이미 출근 처리가 되어 있습니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        attendanceRecordDTO.setEmpIdx(getEmpIdx());
        attendanceRecordDTO.setCoIdx(getCoIdx());
        attendanceRecordDTO.setAttdStatus("AR02");
        attendanceRecordDTO.setAttdTime("9");

        int affectRows = attendanceRecordService.insert(attendanceRecordDTO);

        if (affectRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, "");
        } else {
            result = ResUtil.makeResult("400", "출근 등록 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/out")
    public ResponseEntity<ResResult> out(HttpServletRequest request) {
        ResResult result = null;

        String empIdx = getEmpIdx();
        String ip = getClientIp(request);
        AttendanceRecordDTO attendanceRecordDTO = new AttendanceRecordDTO();

        if (!isCompanyTrustedIp(getCoIdx(), ip)) {
            result = ResUtil.makeResult("400", "사내 IP내에서만 퇴근이 가능합니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        int isAlreadyCheckedOut = attendanceRecordService.isAlreadyCheckedOut(getEmpIdx());

        if (isAlreadyCheckedOut > 0) {
            result = ResUtil.makeResult("400", "이미 퇴근 처리가 되어 있습니다.", null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        attendanceRecordDTO.setEmpIdx(empIdx);
        attendanceRecordDTO.setAttdStatus("AR03");

        int affectRows = attendanceRecordService.update(attendanceRecordDTO);

        if (affectRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, "");
        } else {
            result = ResUtil.makeResult("400", "퇴근 등록 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
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

    @GetMapping("/summary")
    public ResponseEntity<ResResult> getSummary(@RequestParam String date) {
        ResResult result = null;

        AttdSummaryDTO attdSummaryDTO = attendanceRecordService.findAttdSummaryByEmpIdxWithDate(getEmpIdx(), date);

        if (attdSummaryDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdSummaryDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ResResult> getList(@RequestParam String date) {
        ResResult result = null;

        List<AttdDetailDTO> attdDetailDTOs = attendanceRecordService.findAttdDetailListByEmpIdxWithDate(getEmpIdx(),
                date);

        if (attdDetailDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, attdDetailDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
