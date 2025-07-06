package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.exception.CommonException;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.CompanyRoleDTO;
import com.eflix.main.dto.MasterDTO;
import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.dto.SubscriptionBillDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.dto.SubscriptionPackageDetailDTO;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.dto.etc.StatementDTO;
import com.eflix.main.dto.etc.SubscriptionInfoDTO;
import com.eflix.main.dto.etc.SubscriptionProcedureDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.mapper.CompanyRoleMapper;
import com.eflix.main.mapper.MasterMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private MasterMapper masterMapper;

    @Autowired
    private CompanyRoleMapper companyRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SubscriptionPackageDTO findById(String spkIdx) {
        return subscriptionMapper.findById(spkIdx);
    }

    @Override
    public int insertSubscriptionPackageDetail(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO) {
        return subscriptionMapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO);
    }

    @Override
    @Transactional
    public int insertSubscriptionInfo(SubscriptionDTO subscriptionDTO, MasterDTO masterDTO) {
        
        int activeSubscription = 0;
        try {
            activeSubscription = subscriptionMapper.findActiveSubscriptionByCoIdx(subscriptionDTO.getCoIdx());
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

        if(activeSubscription > 0) {
            throw new CommonException("이미 구독 중입니다.");
        }
        subscriptionDTO.setSpiStatus("SS01");

        if(subscriptionMapper.insertSubscription(subscriptionDTO) <= 0) {
            throw new CommonException("구독 정보를 등록하지 못했습니다.");
        }

        SubscriptionPackageDetailDTO subscriptionPackageDetailDTO = new SubscriptionPackageDetailDTO();
        subscriptionPackageDetailDTO.setSpiIdx(subscriptionDTO.getSpiIdx());
        subscriptionPackageDetailDTO.setSpkIdx(subscriptionDTO.getSpkIdx());

        for(String moduleIdx : subscriptionDTO.getCheckedModules()) {
            subscriptionPackageDetailDTO.setModuleIdx(moduleIdx);
            if(subscriptionMapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO) <= 0) {
                throw new CommonException("구독 모듈 저장 실패: " + moduleIdx);
            }

            CompanyRoleDTO companyRoleDTO = new CompanyRoleDTO();
            companyRoleDTO.setCoIdx(subscriptionDTO.getCoIdx());
            if(moduleIdx.equals("mou-101")) {
                companyRoleDTO.setRoleCode("ROLE_HR");
            } else if(moduleIdx.equals("mou-102")) {
                companyRoleDTO.setRoleCode("ROLE_ACC");
            } else if (moduleIdx.equals("mou-103")) {
                companyRoleDTO.setRoleCode("ROLE_PURCHS");
            } else if (moduleIdx.equals("mou-104")) {
                companyRoleDTO.setRoleCode("ROLE_BNZ");
            }
            if(companyRoleMapper.insertCompanyRoleByCoIdx(companyRoleDTO) <= 0) {
                throw new CommonException("회사 권한 저장 실패: " + companyRoleDTO);
            }
        }

        masterDTO.setMstPw(passwordEncoder.encode(masterDTO.getMstPw()));

        if(masterMapper.insertMaster(masterDTO) <= 0) {
            throw new CommonException("마스터 계정을 등록하지 못했습니다.");
        }
        return 1;
    }

    @Override
    public SubscriptionInfoDTO test(String coIdx) {
        return subscriptionMapper.findSubscriptionByCoIdx(coIdx);
    }

    @Override
    public SubscriptionInfoDTO findSubscriptionByCoIdx(String userIdx) {
        CompanyDTO companyDTO = companyMapper.findByUserIdx(userIdx);

        return subscriptionMapper.findSubscriptionByCoIdx(companyDTO.getCoIdx());
    }

    @Override
    public List<SubscriptionInfoDTO> findAllSubscriptionByCoIdx(String coIdx) {
        return subscriptionMapper.findAllSubscriptionByCoIdx(coIdx);
    }

    @Override
    public List<SubscriptionBillDTO> findAllSubscriptionBillByCoIdx(String spiIdx) {
        return subscriptionMapper.findAllSubscriptionBillByCoIdx(spiIdx);
    }

    @Override
    public int findActiveSubscriptionByCoIdx(String coIdx) {
        return subscriptionMapper.findActiveSubscriptionByCoIdx(coIdx);
    }

    @Override
    public StatementDTO findSubscriptionBySpiIdx(String spiIdx) {
        return subscriptionMapper.findSubscriptionBySpiIdx(spiIdx);
    }

    @Override
    public InvoiceDTO findSubscriptionInvoiceBySpiIdx(String spiIdx) {
        return subscriptionMapper.findSubscriptionInvoiceBySpiIdx(spiIdx);
    }

    @Override
    @Transactional
    public void insertSubscriptionByProcedure(SubscriptionProcedureDTO dto) {
        try {
            System.out.println(dto.toString());
            dto.setMstPw(passwordEncoder.encode(dto.getMstPw()));
            subscriptionMapper.callInsertSubscription(dto);
        } catch (Exception e) {
            log.error("구독 프로시저 호출 실패", e);
            throw new CommonException("구독 등록 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public List<ModuleDTO> findAllModuleBySpiIdx(String spiIdx) {
        return subscriptionMapper.findAllModuleBySpiIdx(spiIdx);
    }
}
