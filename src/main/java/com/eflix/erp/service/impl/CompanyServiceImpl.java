package com.eflix.erp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.erp.dto.CompanyDTO;
import com.eflix.erp.mapper.CompanyMapper;
import com.eflix.erp.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public int insertCompany(CompanyDTO companyDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateCompany'");
        return companyMapper.insert(companyDTO);
    }

    @Override
    public CompanyDTO findByUserIdx(String userIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findByUserId'");
        return companyMapper.findByUserIdx(userIdx);
    }

    @Override
    public int updateCompany(CompanyDTO companyDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateCompany'");
        return companyMapper.updateCompany(companyDTO);
    }

}
