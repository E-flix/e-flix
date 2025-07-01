package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.exception.CommonException;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.MasterDTO;
import com.eflix.main.dto.SubscriptionBillDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.dto.SubscriptionPackageDetailDTO;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.dto.etc.StatementDTO;
import com.eflix.main.dto.etc.SubscriptionInfoDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.mapper.MasterMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private MasterMapper masterMapper;

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

        int activeSubscription = subscriptionMapper.findActiveSubscriptionByCoIdx(subscriptionDTO.getCoIdx());

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
        }

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
}
