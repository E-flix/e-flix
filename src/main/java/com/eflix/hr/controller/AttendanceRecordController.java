package com.eflix.hr.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.AttendanceRequestDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.AttendanceRequestService;
import com.eflix.hr.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;


@RequestMapping("/hr")
@Controller
public class AttendanceRecordController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private AttendanceRequestService attendanceRequestService;

    @Autowired
    private EmployeeService employeeService;

    // 근태 현황 화면(사원)
    @GetMapping("/al")
    public String attdList() {
        return "hr/attdList";
    }

    @GetMapping("/al/list")
    @ResponseBody
    public List<AttendanceRecordDTO> getRecordsByMonth(
            @RequestParam String empIdx,
            @RequestParam String yearMonth // e.g. "2025-05"
    ) {
        // 테스트용으로 empIdx 고정하실 거면 유지

        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        AttendanceRecordDTO dto = new AttendanceRecordDTO();
        dto.setEmpIdx(empIdx);
        dto.setCoIdx(AuthUtil.getCoIdx());
        dto.setAttdStart(start.toString()); // "yyyy-MM"
        dto.setAttdEnd(end.toString()); // "yyyy-MM"
        return attendanceRecordService.getRecordsByEmpId(dto);
    }

    @GetMapping("/al/yearmonth")
    @ResponseBody
    public List<LocalDate> getYearMonthList(@RequestParam String empIdx) {
        // empIdx를 무시하고 고정값으로 테스트하신다면 이 한 줄만 유지하셔도 됩니다.
        empIdx = "emp-001";
        return attendanceRecordService.getYearMonthList(AuthUtil.getCoIdx(), empIdx);
    }

    /** 관리자용 근태 리스트 조회 **/
    @GetMapping("/attendance/manage")
    public String manageAttendance(Model model) {
        // 모든 근태 기록, 요청 정보를 가져옴
        List<AttendanceRecordDTO> records = attendanceRecordService.getAllRecords();

        model.addAttribute("records", records);
        return "hr/attendance"; // templates/attendance/manage.html
    }

    // 근태현황 기본항목
    @GetMapping("/al/basicInfo")
    @ResponseBody
    public List<AttendanceRecordDTO> getBasicInfo(@RequestParam String empIdx,
            @RequestParam String yearMonth // e.g. "2025-05"
    ) {
        AttendanceRecordDTO atd = new AttendanceRecordDTO();
        atd.setEmpIdx(empIdx);

        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        atd.setAttdStart(start.toString()); // "yyyy-MM"
        atd.setAttdEnd(end.toString()); // "yyyy-MM"
        atd.setCoIdx("co-101");
        List<AttendanceRecordDTO> basics = attendanceRecordService.getBasicInfo(atd);

        System.out.println("asfsaf:" + basics.toString());
        return basics;
    }

    // 근태 관리화면(관리자) 검색
    @GetMapping("/am/search")
    @ResponseBody
    public List<AttendanceRecordDTO> managerSearch(AttendanceRecordDTO dto) {
        List<AttendanceRecordDTO> data = attendanceRecordService.managerSearch(dto);
        return data;
    }

    @GetMapping("/list")
    public String listPage(Model model) {
        // 검색 폼과 자동 바인딩할 빈 DTO
        model.addAttribute("attendanceRecordDTO", new AttendanceRecordDTO());
        // // select 옵션용 리스트들
        // model.addAttribute("attdStatusList", attendanceRecordService.getAllStatusCodes());
        // model.addAttribute("deptments", attendanceRecordService.findAllDepartments());
        return "attendance/list";  // → src/main/resources/templates/attendance/list.html
    }

    // 근태 관리화면(관리자)
    @GetMapping("/am")
    public String attdMaList() {
        return "hr/attdManager";
    }

  // 근태 신청화면 (사원)
    @GetMapping("/attdAdd")
    public String attdAdd() {
        return "hr/attdAdd";
    }

    //근태 신청 POST
    @PostMapping("/insertAttd")
    @ResponseBody
    public boolean postAttd(@RequestBody AttendanceRequestDTO attendanceRequestDTO) {
        attendanceRequestDTO.setCoIdx(AuthUtil.getCoIdx());
        System.out.println(attendanceRequestDTO.toString());
        int affectedRows = attendanceRequestService.insert(attendanceRequestDTO);
        if(affectedRows > 0) {
            return true;
        }
        return false;
    }
    
    // 근태 신청 승인
    @PostMapping("/updateAttd")
    public boolean updateAttd(AttendanceRequestDTO attendanceRequestDTO) {
        attendanceRequestDTO.setCoIdx(AuthUtil.getCoIdx());
        int affectedRows = attendanceRequestService.update(attendanceRequestDTO);
        if(affectedRows > 0 ) {
            return true;
        }
        return false;
    }
    
    

    // 근태현황 사용자 정보
    // @GetMapping("/al/userInfo")
    // public List<AttendanceRecordDTO> userInfo(@RequestParam String empIdx) {
    // empIdx = "emp-001";
    // List<EmployeeDTO> userInfo = attendanceRecordService.userInfo(empIdx);
    // return userInfo;
    // }

}