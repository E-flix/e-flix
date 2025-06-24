package com.eflix.common.code.service;

import java.util.List;

import com.eflix.common.code.dto.CommonDTO;

public interface CommonService {
   public List<CommonDTO> getCommon(String groupCode);
}
