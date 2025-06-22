package com.eflix.erp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.erp.dto.SubscriptionPackageDTO;
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

}
