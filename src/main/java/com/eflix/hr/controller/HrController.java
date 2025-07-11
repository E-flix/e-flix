package com.eflix.hr.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eflix.common.code.dto.CommonDTO;
import com.eflix.common.code.service.CommonService;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.HrWorkTypeDTO;
import com.eflix.hr.dto.RoleDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.HrWorkTypeService;
import com.eflix.hr.service.RoleService;
import com.eflix.hr.service.SalaryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ============================================
 * - 작성자 : 김어진
 * - 최초작성 : 2025-06-18
 * - 설명 : 인사 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-18 (김어진): 인사 메인 화면 및 각 기능별 화면 요청 처리 메소드 추가
 * - 2025-06-19 (김어진): 각 기능별 화면 요청 처리 메소드 추가
 * - 2025-06-23 (김어진): 사원조회 화면 요청 처리 메소드 추가
 * - 2025-06-25 (김어진): 사원등록 화면 요청 처리 메소드 추가
 * - 2025-06-26 (김어진): 사원수정 화면 요청 처리 메소드 추가
 * ============================================
 */

@Controller
@RequestMapping("/hr")
public class HrController {

    @Value("${upload.path}")
    private String path;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private HrWorkTypeService hrWorkTypeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private CommonService commonService;

    private String getEmpIdx() {
        return AuthUtil.getEmpIdx();
    }

    // 인사 메인 화면
    @GetMapping("")
    public String hrMain() {
        return "hr/hrMain";
    }

    // 사원 관리 화면
    @GetMapping("/el")
    public String empList(
            @RequestParam(value = "option", required = false) String option,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(required = false) String deptUpIdx,
            @RequestParam(required = false) String deptIdx,
            @RequestParam(required = false) String empGrade,
            @RequestParam(required = false) String empStatusType,
            @RequestParam(required = false) String empType,
            Model model) {

        // 부서 드롭다운용 전체 부서 조회
        List<DepartmentDTO> depts = departmentService.findUpAllByCoIdx(AuthUtil.getCoIdx());

        // 사원관리 페이지 직급 드롭다운 조회
        List<EmployeeDTO> gradeList = employeeService.gradeList();

        // 근무유형 드롭다운 조회
        List<HrWorkTypeDTO> workList = hrWorkTypeService.workList();

        // 재직상태 드롭다운 조회
        List<EmployeeDTO> empStatusList = employeeService.empStatusList();

        // 권한 드롭다운 조회
        List<RoleDTO> roleList = roleService.roleList();

        // 은행 드롭다운 조회
        List<CommonDTO> bankList = commonService.getCommon("BK00");

        Map<String, Object> params = new HashMap<>();
        params.put("option", option);
        params.put("keyword", keyword);
        params.put("deptUpIdx", deptUpIdx);
        params.put("deptIdx", deptIdx);
        params.put("empGrade", empGrade);
        params.put("empStatusType", empStatusType);
        params.put("empType", empType);
        params.put("roleList", roleList);

        List<EmployeeDTO> records = employeeService.getAllEmployees(params);

        // 뷰에 넘길 모델
        model.addAttribute("empList", records);
        model.addAttribute("depts", depts);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);
        model.addAttribute("gradeList", gradeList);
        model.addAttribute("workList", workList);
        model.addAttribute("empStatusList", empStatusList);

        // 은행코드
        model.addAttribute("bankList", bankList);

        model.addAllAttributes(params);
        return "hr/emp";
    }

    /** AJAX: 특정 부서(deptUpIdx)의 하위 부서 목록 JSON 리턴 */
    @GetMapping("/{deptUpIdx}/children")
    @ResponseBody
    public List<DepartmentDTO> children(@PathVariable String deptUpIdx) {
        return departmentService.findAllDeptsUP(deptUpIdx);
    }

    @GetMapping("/findEmp")
    @ResponseBody
    public EmployeeDTO findEmp(@RequestParam("empIdx") String empIdx) {
        return employeeService.selectById(empIdx);
    }

    // 사원 등록
    @PostMapping("/insertEmp")
    @ResponseBody
    public String insertEmp(MultipartFile empPhoto, EmployeeDTO emp) throws IllegalStateException, IOException {
        String uploadDir = path + "/hr/emp/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(uploadDir + empPhoto.getOriginalFilename());
        empPhoto.transferTo(dest);

        emp.setEmpImg(empPhoto.getOriginalFilename());
        emp.setCoIdx(AuthUtil.getCoIdx());

        int affectedRows = employeeService.insertEmp(emp);
        if (affectedRows > 0) {
            return "200";
        } else {
            return "404";
        }
    }

    @PostMapping("/updateEmp")
    @ResponseBody
    public String updateEmp(MultipartFile empPhoto, EmployeeDTO emp) throws IllegalStateException, IOException {
        String uploadDir = path + "/hr/emp/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(uploadDir + empPhoto.getOriginalFilename());
        empPhoto.transferTo(dest);

        emp.setEmpImg(empPhoto.getOriginalFilename());
        emp.setCoIdx(AuthUtil.getCoIdx());
        int affectedRows = employeeService.updateEmployee(emp);
        if (affectedRows > 0) {
            return "200";
        } else {
            return "404";
        }
    }

    // 근태 신청승인 화면(관리자)
    @GetMapping("/attdApproval")
    public String attdApproval() {
        return "hr/attdApproval";
    }

    // 급여 계산 화면
    @GetMapping("/sc")
    public String salaryCalculate() {
        return "hr/salaryCalculate";
    }

    // 급여 항목 화면
    @GetMapping("/si")
    public String salaryItem() {
        return "hr/salaryItem";
    }

    // 급여 명세서 화면
    @GetMapping("/sp")
    public String salaryPayslip() {
        return "hr/salaryPayslip";
    }

    // 0708
    @GetMapping("/attd")
    public String attd() {
        return "hr/new/attd/list";
    }

    // 0709
    @GetMapping("/attd/mgr")
    public String attdMgr() {
        return "hr/new/attd/mgr";
    }

    @GetMapping("/attd/req")
    public String req(Model model) {
        EmployeeDTO employeeDTO = employeeService.findByEmpIdx(getEmpIdx());
        DepartmentDTO DepartmentDTO = departmentService.findByEmpIdx(getEmpIdx());
        List<CommonDTO> AttdReqType = commonService.getCommon("AT");

        model.addAttribute("empName", employeeDTO.getEmpName());
        model.addAttribute("empIdx", employeeDTO.getEmpIdx());
        model.addAttribute("deptName", DepartmentDTO.getDeptName());
        model.addAttribute("attdReqType", AttdReqType);
        
        return "hr/new/attd/req";
    }
    
    @GetMapping("/attd/reqMgr")
    public String reqMgr() {
        return "hr/new/attd/reqMgr";
    }

    @GetMapping("/emp")
    public String emp() {
        return "hr/new/emp";
    }

    @GetMapping("/dept")
    public String dept() {
        return "hr/new/dept";
    }
    
    
}
