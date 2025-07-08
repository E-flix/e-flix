package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/list")
    public List<SalarySummaryDTO> getList(@RequestParam String coIdx,
            @RequestParam(required = false) String salaryMonth,
            @RequestParam(required = false) String payMonth,
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String deptIdx) {
        return salaryService.findSalaryList(coIdx, salaryMonth, payMonth, empName, deptIdx);
    }

    @GetMapping("/detail")
    public List<SalaryDetailDTO> findSalaryDetail(@RequestParam String coIdx, @RequestParam String salaryIdx) {
        return salaryService.findSalaryDetail(coIdx, salaryIdx);
    }

    @GetMapping("/detail-items")
    public List<SalaryDetailDTO> getSalaryDetailItems(
            @RequestParam String coIdx,
            @RequestParam String salaryIdx) {
        return salaryService.selectSalaryDetail(coIdx, salaryIdx);
    }

    @PostMapping("/calc")
    public void postCal(@RequestParam String coIdx, @RequestParam List<String> salaryIdxList) {
                Map<String, Object> map = new HashMap<>();
                map.put("coIdx", coIdx);
                map.put("salaryIdxList", salaryIdxList);
        salaryService.calculateSalary(map);
    }
    

    @PostMapping("/confirm")
    public void postConfirm(@RequestParam String coIdx,
            @RequestParam List<String> salaryIdxList) {
                Map<String, Object> map = new HashMap<>();
                map.put("coIdx", coIdx);
                map.put("salaryIdxList", salaryIdxList);
        salaryService.confirmSalary(map);
    }
    
    // 0707
    @GetMapping("/items")
    public List<SalaryMappingDTO> getItems(@RequestParam("coIdx") String coIdx) {
        return salaryMappingService.findAllByCoIdx(coIdx);
    }

    @GetMapping("/item/{mpIdx}")
    public SalaryMappingDTO getItem(@PathVariable("mpIdx") String mpIdx, @RequestParam("coIdx") String coIdx) {
        return salaryMappingService.findByMpIdx(coIdx, mpIdx);
    }

    @PostMapping("/item")
    public void postItem(@ModelAttribute SalaryMappingDTO salaryMappingDTO) {
        
        System.out.println(salaryMappingDTO.toString());
        salaryMappingService.insert(salaryMappingDTO);
    }

    @PutMapping("/item/{mpIdx}")
    public void putItem(@PathVariable("mpIdx") String mpIdx, @ModelAttribute SalaryMappingDTO salaryMappingDTO) {
        System.out.println(salaryMappingDTO);
        salaryMappingDTO.setMpIdx(mpIdx);
        salaryMappingService.update(salaryMappingDTO);
    }

    @DeleteMapping("/item/{mpIdx}")
    public void deleteItem(@PathVariable("mpIdx") String mpIdx) {
        salaryMappingService.delete(mpIdx);
    }
}
