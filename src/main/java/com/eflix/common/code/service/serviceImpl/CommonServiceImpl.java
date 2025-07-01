package com.eflix.common.code.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.common.code.dto.CommonDTO;
import com.eflix.common.code.mapper.CommonMapper;
import com.eflix.common.code.service.CommonService;
@Service
public class CommonServiceImpl implements CommonService{

    @Autowired
    CommonMapper commonMapper;

    public List<CommonDTO> getCommon(String groupCode) {
        return commonMapper.getCommon(groupCode);
    }
}
