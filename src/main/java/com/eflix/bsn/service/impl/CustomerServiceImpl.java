package com.eflix.bsn.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.mapper.CustomerMapper;
import com.eflix.bsn.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO getCustomerInfo(String customerCd) {
        return customerMapper.selectByCustomerCd(customerCd);
    }

    public CustomerServiceImpl(CustomerMapper mapper) {
        this.customerMapper = mapper;
    }

    @Override
    public List<CustomerDTO> findAll() {
        return customerMapper.selectAll();
    }

    @Override
    public List<CustomerDTO> findByName(String name) {
        return customerMapper.selectByName(name);
    }

    @Override
    public Map<String, Object> getCustomerWithCredit(String customerCd) {
        return customerMapper.selectCustomerWithCredit(customerCd);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerMapper.searchCustomers(keyword);
    }
}
