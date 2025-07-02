package com.eflix.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.service.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


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
public class CompnayRestController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/find")
    public ResponseEntity<ResResult> findByUserId(@RequestParam("userIdx") String userIdx) {
        ResResult result = null;

        CompanyDTO companyDTO = companyService.findByUserIdx(userIdx);

        if(companyDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, companyDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ResResult> my(@AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        ResResult result = null;

        CompanyDTO companyDTO = companyService.findByUserIdx(securityUserDetails.getSecurityUserDTO().getUserIdx());

        if(companyDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, companyDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResResult> insert(@RequestPart("companyData") CompanyDTO companyDTO, @RequestPart("bizrCert") MultipartFile bizrCert) {
        //TODO: process POST request
        ResResult result;

        companyDTO.setBizrCert(bizrCert.getOriginalFilename());
        int affectedRows = companyService.insertCompany(companyDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "회사 정보를 등록 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResResult> update(@RequestBody CompanyDTO companyDTO) {
        //TODO: process POST request
        ResResult result;
        
        int affectedRows = companyService.updateCompany(companyDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "회사 정보를 수정 하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
