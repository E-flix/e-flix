package com.eflix.bsn.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.mapper.CustomerMapper;
import com.eflix.bsn.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<CustomerDTO> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<CustomerDTO> findByName(String name) {
        return mapper.selectByName(name);
    }
}
