package com.eflix.erp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.result.ResResult;
import com.eflix.erp.dto.CompanyDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ERP 회사 관리를 위한 Controller 클래스
 * 
 * <p>
 * 이 클래스는 ERP 시스템 내 회사 정보를 등록, 수정, 삭제, 조회하는 기능을 제공합니다.
 * 주로 사용자(User)를 대상으로 하며, 회사 기본 정보를 효율적으로 관리할 수 있도록 지원합니다.
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 *   <li>회사 정보 등록</li>
 *   <li>회사 정보 수정</li>
 *   <li>회사 정보 삭제</li>
 *   <li>회사 정보 조회</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-24
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-24: 최초 생성 (복성민)</li>
 * </ul>
 */

@RestController
@RequestMapping("/erp/company")
public class CompnayController {
    
    @PostMapping("/update")
    public ResponseEntity<ResResult> update(@RequestBody CompanyDTO companyDTO) {
        //TODO: process POST request
        ResResult result = null;
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
