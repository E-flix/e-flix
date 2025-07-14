package com.eflix.main.mapper;

import java.util.Date;
import java.util.List;

import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.CompanySearchDTO;
import com.eflix.main.dto.etc.CompanySubscriptionDTO;

public interface CompanyMapper {
    
    public int insert(CompanyDTO companyDTO);

    public CompanyDTO findByUserIdx(String userIdx);

    public int updateCompany(CompanyDTO companyDTO);

    public int findAllCompanyCount(CompanySearchDTO companySearchDTO);

    public List<CompanyDTO> findAllCompany(CompanySearchDTO companySearchDTO);

    public CompanyDTO findByCoIdx(String coIdx);

    public List<CompanySubscriptionDTO> findAllCompanyWithSubscription();

    // 0703
    public SubscriptionDTO findSubscriptionByCoIdx(String coIdx);

    public Date findCoRegdateByCoIdx(String coIdx);
}
