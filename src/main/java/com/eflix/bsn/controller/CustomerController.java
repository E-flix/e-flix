package com.eflix.bsn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.service.CustomerService;

@RestController
@RequestMapping("/bsn/customers")
public class CustomerController {

    
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    /** 전체 거래처 조회 */
    @GetMapping
    public List<CustomerDTO> searchCustomers(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return service.findByName(name);
        }
        return service.findAll();
    }
}
