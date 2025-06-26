package com.eflix.main.mapper;

import com.eflix.main.dto.CompanyDTO;

public interface CompanyMapper {
    
    public int insert(CompanyDTO companyDTO);

    public CompanyDTO findByUserIdx(String userIdx);

    public int updateCompany(CompanyDTO companyDTO);
}
