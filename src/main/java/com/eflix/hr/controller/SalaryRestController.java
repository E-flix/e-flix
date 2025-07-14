package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.eflix.common.jasperResolver.JasperDownloadPDF;
import com.eflix.common.jasperResolver.JasperPreviewPDF;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.etc.SalaryCalcDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryEmpDTO;
import com.eflix.hr.dto.etc.SalaryListDTO;
import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.dto.etc.SalaryMappingSearchDTO;
import com.eflix.hr.dto.etc.SalarySearchDTO;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.SalaryMappingService;
import com.eflix.hr.service.SalaryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/hr/salary")
public class SalaryRestController {

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private SalaryMappingService salaryMappingService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JasperPreviewPDF jasperPreviewPDF;

    @Autowired
    private JasperDownloadPDF jasperDownloadPDF;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    public String getEmpIdx() {
        return AuthUtil.getEmpIdx();
    }

    private final String STUB_REPORT_PATH = "/reports/stub_template.jasper";

    // 0712
    @GetMapping("/items")
    public ResponseEntity<ResResult> getItems(SalaryMappingSearchDTO salaryMappingSearchDTO) {
        ResResult result = null;

        salaryMappingSearchDTO.setCoIdx(getCoIdx());

        int itemCount = salaryMappingService.findAllItemCount(salaryMappingSearchDTO);

        salaryMappingSearchDTO.setTotalRecord(itemCount);

        List<SalaryMappingDTO> items = salaryMappingService.findAllBySearch(salaryMappingSearchDTO);

        if(items != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("items", items);
            searchResult.put("total", itemCount);
            searchResult.put("page", salaryMappingSearchDTO.getPage());
            searchResult.put("startPage", salaryMappingSearchDTO.getStartPage());
            searchResult.put("pageSize", salaryMappingSearchDTO.getPageUnit());
            searchResult.put("endPage", salaryMappingSearchDTO.getEndPage());
            searchResult.put("lastPage", salaryMappingSearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/item")
    public ResponseEntity<ResResult> getItems(@RequestBody SalaryMappingDTO salaryMappingDTO) {
        ResResult result = null;

        salaryMappingDTO.setCoIdx(getCoIdx());
        int affectedRows = salaryMappingService.insert(salaryMappingDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "등록에 실패했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/item/{mpIdx}")
    public ResponseEntity<ResResult> findItem(@PathVariable("mpIdx") String mpIdx) {
        ResResult result = null;

        SalaryMappingDTO salaryMappingDTO = salaryMappingService.findByMpIdx(mpIdx);

        if(salaryMappingDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, salaryMappingDTO);
        } else {
            result = ResUtil.makeResult("400", "등록된 데이터가 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PutMapping("/item/{mpIdx}")
    public ResponseEntity<ResResult> updateItem(@PathVariable("mpIdx") String mpIdx, @RequestBody SalaryMappingDTO salaryMappingDTO) {
        ResResult result = null;

        salaryMappingDTO.setMpIdx(mpIdx);
        int affectedRows = salaryMappingService.update(salaryMappingDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "수정에 실패했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/item/{mpIdx}")
    public ResponseEntity<ResResult> deleteItem(@PathVariable("mpIDx") String mpIdx) {
        ResResult result = null;

        int affectedRows = salaryMappingService.delete(mpIdx);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "삭제에 실패했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/list")
    public ResponseEntity<ResResult> list(SalarySearchDTO salarySearchDTO) {
        ResResult result = null;

        salarySearchDTO.setCoIdx(getCoIdx());

        System.out.println(salarySearchDTO.toString());

        int salaryCount = salaryService.findAllCountBySearch(salarySearchDTO);

        salarySearchDTO.setTotalRecord(salaryCount);

        List<SalaryListDTO> salaries = salaryService.findAllBySearch(salarySearchDTO);

        if(salaries != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("salaries", salaries);
            searchResult.put("total", salaryCount);
            searchResult.put("page", salarySearchDTO.getPage());
            searchResult.put("startPage", salarySearchDTO.getStartPage());
            searchResult.put("pageSize", salarySearchDTO.getPageUnit());
            searchResult.put("endPage", salarySearchDTO.getEndPage());
            searchResult.put("lastPage", salarySearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/calc")
    public ResponseEntity<ResResult> salary(@RequestBody SalaryCalcDTO salaryCalcDTO) {
        ResResult result = null;
        
        salaryCalcDTO.setCoIdx(getCoIdx());
        int affectedRows = salaryService.calcSalaryByEmpIdx(salaryCalcDTO);

        if (affectedRows == salaryCalcDTO.getEmpIdxList().size()) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else if (affectedRows < salaryCalcDTO.getEmpIdxList().size() && affectedRows > 0) {
            result = ResUtil.makeResult("400", "일부 사원의 급여 계산 과정 중 오류가 발생했습니다.", null);
        } else {
            result = ResUtil.makeResult("400", "급여 계산 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/detail")
    public ResponseEntity<ResResult> getMethodName(
            @RequestParam("salaryIdx") String salaryIdx,
            @RequestParam("empIdx") String empIdx) {
        ResResult result = null;

        List<SalaryDetailDTO> salaryDetailDTO = salaryService.salaryDetailBySalaryIdxWithCoIdx(salaryIdx, getCoIdx());
        SalaryEmpDTO salaryEmpDTO = salaryService.salaryEmpInfo(empIdx, salaryIdx);

        if(salaryDetailDTO != null && salaryEmpDTO != null) {
            Map<String, Object> detail = new HashMap<>();
            detail.put("items", salaryDetailDTO);
            detail.put("emp", salaryEmpDTO);
            result = ResUtil.makeResult(ResStatus.OK, detail);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PutMapping("/confirm")
    public ResponseEntity<ResResult> confirm(@RequestBody List<String> salaryIdxList) {
        ResResult result = null;

        int affectedRows = salaryService.confirmSalary(salaryIdxList);
        if(affectedRows == salaryIdxList.size()) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else if (affectedRows < salaryIdxList.size() || affectedRows > 0) {
            result = ResUtil.makeResult("400", "일부 사원의 급여 확정 과정 중 오류가 발생했습니다.", null);
        } else {
            result = ResUtil.makeResult("400", "급여 확정 과정 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/report/preview/stub/{empIdx}/{date}")
    public ModelAndView previewStub(@PathVariable("empIdx") String empIdx, @PathVariable("date") String date) {
        ModelAndView mv = new ModelAndView();

        String salaryIdx = salaryService.findSalaryIdxByEmpIdxAndDate(getEmpIdx(), date);

        List<SalaryDetailDTO> detailList = salaryService.salaryDetailBySalaryIdxWithCoIdx(salaryIdx, getCoIdx());
        SalaryEmpDTO salaryEmpDTO = salaryService.salaryEmpInfo(empIdx, salaryIdx);

        if(detailList == null || salaryEmpDTO == null) {
            throw new RuntimeException("급여 명세 데이터를 찾을 수 없습니다.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("employeeName", salaryEmpDTO.getEmpName());
        params.put("employeeId",   salaryEmpDTO.getEmpIdx());
        params.put("department",   salaryEmpDTO.getDeptName());
        params.put("position",     salaryEmpDTO.getGrdName());
        params.put("payDate",      salaryEmpDTO.getPayMonth());
        params.put("companyName",  "E-FLIX");

        mv.setView(jasperPreviewPDF);
        mv.addObject("fileName", STUB_REPORT_PATH);
        mv.addObject("dataList",   detailList);
        mv.addObject("params", params);
        mv.addObject("saveName", "stub" + salaryEmpDTO.getEmpName() + "_" + salaryEmpDTO.getPayMonth());

        return mv;
    }

    
    
    // // 0707
    // @GetMapping("/list")
    // public List<SalarySummaryDTO> getList(
    //         @RequestParam(required = false) String attMonth,
    //         @RequestParam(required = false) String payMonth,
    //         @RequestParam(required = false) String empName,
    //         @RequestParam(required = false) String deptIdx) {
    //     return salaryService.findSalaryList(getCoIdx(), attMonth, payMonth, empName, deptIdx);
    // }

    // @GetMapping("/detail")
    // public List<SalaryDetailDTO> findSalaryDetail(@RequestParam String salaryIdx) {
    //     return salaryService.findSalaryDetail(getCoIdx(), salaryIdx);
    // }

    // @GetMapping("/detail-items")
    // public List<SalaryDetailDTO> getSalaryDetailItems(
    //         @RequestParam String salaryIdx) {
    //     return salaryService.selectSalaryDetail(getCoIdx(), salaryIdx);
    // }

    // @PostMapping("/calc")
    // public void postCal(@RequestParam List<String> salaryIdxList) {
    //     System.out.println();
    //     salaryService.calculateSalary(getCoIdx(), salaryIdxList);
    // }

    // @PostMapping("/confirm")
    // public void postConfirm(
    //         @RequestParam List<String> salaryIdxList) {
    //             Map<String, Object> map = new HashMap<>();
    //             map.put("coIdx", getCoIdx());
    //             map.put("salaryIdxList", salaryIdxList);
    //     salaryService.confirmSalary(map);
    // }
    // @GetMapping("/items")
    // public List<SalaryMappingDTO> getItems() {
    //     return salaryMappingService.findAllByCoIdx(getCoIdx());
    // }

    // @GetMapping("/item/{mpIdx}")
    // public SalaryMappingDTO getItem(@PathVariable("mpIdx") String mpIdx) {
    //     return salaryMappingService.findByMpIdx(getCoIdx(), mpIdx);
    // }

    // @PostMapping("/item")
    // public void postItem(@ModelAttribute SalaryMappingDTO salaryMappingDTO) {
    //     salaryMappingService.insert(salaryMappingDTO);
    // }

    // @PutMapping("/item/{mpIdx}")
    // public void putItem(@PathVariable("mpIdx") String mpIdx, @ModelAttribute SalaryMappingDTO salaryMappingDTO) {
    //     salaryMappingDTO.setMpIdx(mpIdx);
    //     salaryMappingService.update(salaryMappingDTO);
    // }

    // @DeleteMapping("/item/{mpIdx}")
    // public void deleteItem(@PathVariable("mpIdx") String mpIdx) {
    //     salaryMappingService.delete(mpIdx);
    // }

}
