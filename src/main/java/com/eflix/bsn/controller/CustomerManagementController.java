package com.eflix.bsn.controller;

import com.eflix.bsn.dto.CustomerWithCreditDTO;
import com.eflix.bsn.service.CustomerManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bsn/customer-management")
@RequiredArgsConstructor
public class CustomerManagementController {

    private final CustomerManagementService customerManagementService;

    /**
     * 전체 거래처 목록 조회 (여신 요약 정보 포함)
     */
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getCustomerList() {
        List<Map<String, Object>> list = customerManagementService.getCustomerListWithCreditSummary();
        return ResponseEntity.ok(list);
    }

    /**
     * 특정 거래처 상세 정보 조회 (여신 정보 포함)
     */
    @GetMapping("/{customerCd}")
    public ResponseEntity<CustomerWithCreditDTO> getCustomerDetails(@PathVariable String customerCd) {
        CustomerWithCreditDTO customerDetails = customerManagementService.getCustomerWithCredit(customerCd);
        if (customerDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerDetails);
    }

    /**
     * 신규 거래처 또는 기존 거래처 정보 저장 (여신 정보 포함)
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveCustomerWithCredit(@RequestBody CustomerWithCreditDTO dto) {
        try {
            Map<String, Object> result = customerManagementService.saveCustomerWithCredit(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
