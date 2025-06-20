package com.eflix.erp.dto;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 마스터 정보를 전송하기 위한 DTO 클래스
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 * </ul>
 */

@Data
@Builder
public class MasterDTO {
    private String mstIdx;          // mst-000
    private String coIdx;           // co-000
    private String mstId;
    private String mstPw;
    private String mstName;
    private String mstEmail;
}
