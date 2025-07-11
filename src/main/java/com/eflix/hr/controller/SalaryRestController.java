package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.security.auth.AuthUtil;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;
import com.eflix.hr.service.SalaryMappingService;
import com.eflix.hr.service.SalaryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/hr/sc")
public class SalaryRestController {

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private SalaryMappingService salaryMappingService;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }
    
    // 0707
    @GetMapping("/list")
    public List<SalarySummaryDTO> getList(
            @RequestParam(required = false) String attMonth,
            @RequestParam(required = false) String payMonth,
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String deptIdx) {
        return salaryService.findSalaryList(getCoIdx(), attMonth, payMonth, empName, deptIdx);
    }

    @GetMapping("/detail")
    public List<SalaryDetailDTO> findSalaryDetail(@RequestParam String salaryIdx) {
        return salaryService.findSalaryDetail(getCoIdx(), salaryIdx);
    }

    @GetMapping("/detail-items")
    public List<SalaryDetailDTO> getSalaryDetailItems(
            @RequestParam String salaryIdx) {
        return salaryService.selectSalaryDetail(getCoIdx(), salaryIdx);
    }

    @PostMapping("/calc")
    public void postCal(@RequestParam List<String> salaryIdxList) {
        System.out.println();
        salaryService.calculateSalary(getCoIdx(), salaryIdxList);
    }

    @PostMapping("/confirm")
    public void postConfirm(
            @RequestParam List<String> salaryIdxList) {
                Map<String, Object> map = new HashMap<>();
                map.put("coIdx", getCoIdx());
                map.put("salaryIdxList", salaryIdxList);
        salaryService.confirmSalary(map);
    }
    @GetMapping("/items")
    public List<SalaryMappingDTO> getItems() {
        return salaryMappingService.findAllByCoIdx(getCoIdx());
    }

    @GetMapping("/item/{mpIdx}")
    public SalaryMappingDTO getItem(@PathVariable("mpIdx") String mpIdx) {
        return salaryMappingService.findByMpIdx(getCoIdx(), mpIdx);
    }

    @PostMapping("/item")
    public void postItem(@ModelAttribute SalaryMappingDTO salaryMappingDTO) {
        salaryMappingService.insert(salaryMappingDTO);
    }

    @PutMapping("/item/{mpIdx}")
    public void putItem(@PathVariable("mpIdx") String mpIdx, @ModelAttribute SalaryMappingDTO salaryMappingDTO) {
        salaryMappingDTO.setMpIdx(mpIdx);
        salaryMappingService.update(salaryMappingDTO);
    }

    @DeleteMapping("/item/{mpIdx}")
    public void deleteItem(@PathVariable("mpIdx") String mpIdx) {
        salaryMappingService.delete(mpIdx);
    }
}
