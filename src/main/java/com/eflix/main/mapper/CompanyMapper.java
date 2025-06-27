package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.etc.CompanySearchDTO;

public interface CompanyMapper {
    
    public int insert(CompanyDTO companyDTO);

    public CompanyDTO findByUserIdx(String userIdx);

    public int updateCompany(CompanyDTO companyDTO);

    public int findAllCompanyCount(CompanySearchDTO companySearchDTO);

    public List<CompanyDTO> findAllCompany(CompanySearchDTO companySearchDTO);
}
