package com.eflix.main.service;

import com.eflix.main.dto.CompanyDTO;

/**
 * ERP 회사 관리를 위한 Service 클래스
 * 
 * <p>
 * 회사 등록, 정보 관리, 사업자 등록증 처리 및 회사 마스터 계정 관리 등의 
 * 비즈니스 로직을 처리합니다. ERP 시스템 사용을 위한 회사 정보 전반을 관리합니다.
 * </p>
 * 
* <h3>주요 기능</h3>
 * <ul>
 *   <li>회사 등록 및 사업자등록번호 검증</li>
 *   <li>회사 정보 조회 및 수정</li>
 *   <li>사업자 등록증 파일 업로드 처리</li>
 *   <li>회사 마스터 계정 생성 및 관리</li>
 *   <li>서비스 상태 관리 (구독전/구독중/만료)</li>
 * </ul>
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

public interface CompanyService {
    public int insertCompany(CompanyDTO companyDTO);

    public CompanyDTO findByUserIdx(String userIdx);

    public int updateCompany(CompanyDTO companyDTO);
}
