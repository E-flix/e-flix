package com.eflix.main.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.CompanySearchDTO;
import com.eflix.main.dto.etc.CompanySubscriptionDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public int insertCompany(CompanyDTO companyDTO) {
        return companyMapper.insert(companyDTO);
    }

    @Override
    public CompanyDTO findByUserIdx(String userIdx) {
        return companyMapper.findByUserIdx(userIdx);
    }

    @Override
    public int updateCompany(CompanyDTO companyDTO) {
        return companyMapper.updateCompany(companyDTO);
    }

    @Override
    public int findAllCompanyCount(CompanySearchDTO companySearchDTO) {
        return companyMapper.findAllCompanyCount(companySearchDTO);
    }

    @Override
    public List<CompanyDTO> findAllCompany(CompanySearchDTO companySearchDTO) {
        return companyMapper.findAllCompany(companySearchDTO);
    }

    @Override
    public CompanyDTO findByCoIdx(String coIdx) {
        return companyMapper.findByCoIdx(coIdx);
    }

    @Override
    public List<CompanySubscriptionDTO> findAllCompanyWithSubscription() {
        return companyMapper.findAllCompanyWithSubscription();
    }

    // 0703
    public SubscriptionDTO findSubscriptionByCoIdx(String coIdx) {
        return companyMapper.findSubscriptionByCoIdx(coIdx);
    }

    @Override
    public Date findCoRegdateByCoIdx(String coIdx) {
        return companyMapper.findCoRegdateByCoIdx(coIdx);
    }
}
