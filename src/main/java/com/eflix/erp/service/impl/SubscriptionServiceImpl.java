package com.eflix.erp.service.impl;

import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.exception.SubscriptionException;
import com.eflix.erp.dto.SubscriptionDTO;
import com.eflix.erp.dto.SubscriptionPackageDTO;
import com.eflix.erp.dto.SubscriptionPackageDetailDTO;
import com.eflix.erp.mapper.SubscriptionMapper;
import com.eflix.erp.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionMapper mapper;

    @Override
    public SubscriptionPackageDTO findById(String spkIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findById'");
        return mapper.findById(spkIdx);
    }

    @Override
    public int insertSubscriptionPackageDetail(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertSubscriptionPackageDetail'");
        return mapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO);
    }

    @Override
    @Transactional
    public int insertSubscriptionInfo(SubscriptionDTO subscriptionDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertSubscriptionInfo'");

        subscriptionDTO.setSpiStatus("SS01");

        if(mapper.insertSubscription(subscriptionDTO) <= 0) {
            throw new SubscriptionException("구독 정보를 등록하지 못했습니다.");
        }

        SubscriptionPackageDetailDTO subscriptionPackageDetailDTO = new SubscriptionPackageDetailDTO();
        subscriptionPackageDetailDTO.setSpiIdx(subscriptionDTO.getSpiIdx());
        subscriptionPackageDetailDTO.setSpkIdx(subscriptionDTO.getSpkIdx());

        for(String moduleIdx : subscriptionDTO.getCheckedModules()) {
            subscriptionPackageDetailDTO.setModuleIdx(moduleIdx);
            if(mapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO) <= 0) {
                throw new SubscriptionException("구독 모듈 저장 실패: " + moduleIdx);
            }
        }
        
        return 1;
    }

}
