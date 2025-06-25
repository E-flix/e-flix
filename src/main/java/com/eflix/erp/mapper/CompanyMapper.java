package com.eflix.erp.mapper;

import com.eflix.erp.dto.CompanyDTO;

public interface CompanyMapper {
    
    public int insert(CompanyDTO companyDTO);

    public CompanyDTO findByUserIdx(String userIdx);

    public int updateCompany(CompanyDTO companyDTO);
}
