package com.eflix.main.dto.etc;

import java.util.Date;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class CompanySearchDTO extends PagingDTO {
    private String coIdx;         // co-000
    private String userIdx;       // user-000
    private String coName;
    private String coNameEn;
    private String coAddr;
    private String coAddrDetail;
    private Integer coZip;
    private String bizrNo;
    private String bizrCert;
    private String ceoName;
    private String ceoTel;
    private String pschName;
    private String pschTel;
    private String pschEmail;
    private String svcStatus;
    private Date coRegdate;

    // offset 계산
    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    // limit 가져오기
    public int getLimit() {
        return getPageUnit();
    }
}
