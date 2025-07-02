package com.eflix.hr.controller;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.service.AttendanceRecordService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/hr")
@Controller
public class AttendanceRecordController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

      // 근태 현황 화면(사원)
    @GetMapping("/al")
    public String attdList(AttendanceRecordDTO atd, Model model) {
        List<AttendanceRecordDTO> attdList = attendanceRecordService.getRecordsByEmpId("emp-001");
        model.addAttribute("attdList", attdList);
        return "hr/attdList";
    }
    //------------------------- 근태현황 로그인 사원 년월 드롭다운 가져오기 ------------------------
    // @GetMapping("/attendance")
    // public String attendancePage(@RequestParam String empIdx, Model model) {
    //     // 1) 사원 입사일(또는 근무 시작일)을 가져온다
    //     LocalDate joinDate = attendanceRecordService.getJoinDate(empIdx);
    //     // 2) YearMonth 객체로 변환
    //     YearMonth startYm = YearMonth.from(joinDate);
    //     YearMonth endYm   = YearMonth.now();             // 또는 파라미터로 받은 조회 종료 월

    //     // 3) startYm부터 endYm까지 한 달씩 증가시키며 리스트에 담기
    //     List<String> yearMonths = new ArrayList<>();
    //     YearMonth ym = startYm;
    //     while (!ym.isAfter(endYm)) {
    //         yearMonths.add(ym.getYear() + "년 " + ym.getMonthValue() + "월");
    //         ym = ym.plusMonths(1);
    //     }

    //     model.addAttribute("yearMonths", yearMonths);
    //     return "attendance";  // thymeleaf 뷰 이름
    // }
// --------------------------------------------------------------------------------------------------
    /** 관리자용 근태 리스트 조회 */
    @GetMapping("/attendance/manage")
    public String manageAttendance(Model model) {
        // 모든 근태 기록, 요청 정보를 가져옴
        List<AttendanceRecordDTO> records = attendanceRecordService.getAllRecords();

        model.addAttribute("records", records);
        return "hr/attendance";  // templates/attendance/manage.html
    }
}