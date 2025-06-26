package com.eflix.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.erp.dto.AnswerDTO;
import com.eflix.erp.dto.QuestionDTO;
import com.eflix.erp.service.InquiryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ERP 문의 관리를 위한 Controller 클래스
 * 
 * <p>
 * 이 클래스는 ERP 시스템 내 회사 문의를 등록, 수정, 삭제, 조회하는 기능을 제공합니다.
 * 주로 사용자(User)와 관리자를 대상으로 하며, 문의 기본 정보를 효율적으로 관리할 수 있도록 지원합니다.
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-25
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-25: 최초 생성 (복성민)</li>
 * </ul>
 */


@RestController
@RequestMapping("/erp/inquiry")
public class InquiryRestController {
    
    @Autowired
    private InquiryService inquiryService;

    @GetMapping()
    public ResponseEntity<ResResult> getInquiry(@RequestParam("qstIdx") String qstIdx) {
        ResResult result = null;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    
    @GetMapping("/qst")
    public ResponseEntity<ResResult> getQst(@RequestParam("qstIdx") String qstIdx) {
        ResResult result = null;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/ans")
    public ResponseEntity<ResResult> getAns(@RequestParam("ansIdx") String ansIdx) {
        ResResult result = null;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @PostMapping("/qst")
    public ResponseEntity<ResResult> qst(@RequestBody QuestionDTO questionDTO) {
        ResResult result = null;
        System.out.println(questionDTO.toString());
        int affectedRows = inquiryService.insertQst(questionDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "문의 등록 중 오류가 발생했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/ans")
    public ResponseEntity<ResResult> ans(@RequestBody AnswerDTO answerDTO) {
        ResResult result = null;

        int affectedRows = inquiryService.insertAns(answerDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "답변 등록 중 오류가 발생했습니다.", null);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
