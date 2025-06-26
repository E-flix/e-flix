package com.eflix.erp.dto.etc;

import java.util.List;

import com.eflix.erp.dto.ModuleDTO;
import com.eflix.erp.dto.SubscriptionDTO;
import com.eflix.erp.dto.SubscriptionPackageDTO;

import lombok.Data;

@Data
public class SubscriptionInfoDTO {
    private String spiIdx;                          // 해당 테이블의 기준
    private SubscriptionDTO subscriptionDTO;
    private SubscriptionPackageDTO subscriptionPackageDTO;
    private List<ModuleDTO> moduleDTOs;
}
