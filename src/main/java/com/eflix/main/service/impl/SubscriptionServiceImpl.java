package com.eflix.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.exception.Exception;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.MasterDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.dto.SubscriptionPackageDetailDTO;
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
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findById'");
        return subscriptionMapper.findById(spkIdx);
    }

    @Override
    public int insertSubscriptionPackageDetail(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertSubscriptionPackageDetail'");
        return subscriptionMapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO);
    }

    @Override
    @Transactional
    public int insertSubscriptionInfo(SubscriptionDTO subscriptionDTO, MasterDTO masterDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertSubscriptionInfo'");

        subscriptionDTO.setSpiStatus("SS01");

        if(subscriptionMapper.insertSubscription(subscriptionDTO) <= 0) {
            throw new Exception("구독 정보를 등록하지 못했습니다.");
        }

        SubscriptionPackageDetailDTO subscriptionPackageDetailDTO = new SubscriptionPackageDetailDTO();
        subscriptionPackageDetailDTO.setSpiIdx(subscriptionDTO.getSpiIdx());
        subscriptionPackageDetailDTO.setSpkIdx(subscriptionDTO.getSpkIdx());

        for(String moduleIdx : subscriptionDTO.getCheckedModules()) {
            subscriptionPackageDetailDTO.setModuleIdx(moduleIdx);
            if(subscriptionMapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO) <= 0) {
                throw new Exception("구독 모듈 저장 실패: " + moduleIdx);
            }
        }

        if(masterMapper.insertMaster(masterDTO) <= 0) {
            throw new Exception("마스터 계정을 등록하지 못했습니다.");
        }
        
        return 1;
    }

    @Override
    public SubscriptionInfoDTO test(String coIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findSubscriptionByCoIdx'");
        return subscriptionMapper.findSubscriptionByCoIdx(coIdx);
    }

    @Override
    public SubscriptionInfoDTO findSubscriptionByCoIdx(String userIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getSubscriptionInfoByCoIdx'");
        CompanyDTO companyDTO = companyMapper.findByUserIdx(userIdx);

        return subscriptionMapper.findSubscriptionByCoIdx(companyDTO.getCoIdx());
    }

}
