package com.eflix.bsn.service;

import java.util.List;
import com.eflix.bsn.dto.CustomerDTO;

public interface CustomerService {
    List<CustomerDTO> findAll();
    List<CustomerDTO> findByName(String name);
}
