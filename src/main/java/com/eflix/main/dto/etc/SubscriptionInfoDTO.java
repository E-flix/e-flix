package com.eflix.main.dto.etc;

import java.util.Date;
import java.util.List;

import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;

import lombok.Data;

@Data
public class SubscriptionInfoDTO {
    private String spiIdx;                          // 해당 테이블의 기준
    private Date recentPayDate;
    private SubscriptionDTO subscriptionDTO;
    private SubscriptionPackageDTO subscriptionPackageDTO;
    private CompanyDTO companyDTO;
    private List<ModuleDTO> moduleDTOs;
}
