package com.eflix.main.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 회사 정보를 전송하기 위한 DTO 클래스
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see com.eflix.erp.service.DepartmentService
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 * </ul>
 */

@Data
@Builder
public class CompanyDTO {
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
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date coRegdate;
}
