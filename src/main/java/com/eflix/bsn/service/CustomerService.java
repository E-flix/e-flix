package com.eflix.bsn.service;

import java.util.List;
import java.util.Map;

import com.eflix.bsn.dto.CustomerDTO;

public interface CustomerService {
    
    CustomerDTO getCustomerInfo(String customerCd);
    Map<String, Object> getCustomerWithCredit(String customerCd);
    List<CustomerDTO> searchCustomers(String keyword);
    List<CustomerDTO> searchCustomersByName(String name);
    List<CustomerDTO> findAll();
    List<CustomerDTO> findByName(String name);
}
