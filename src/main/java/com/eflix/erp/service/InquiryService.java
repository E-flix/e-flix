package com.eflix.erp.service;

import com.eflix.erp.dto.AnswerDTO;
import com.eflix.erp.dto.QuestionDTO;

/**
 * ERP 고객 문의 관리를 위한 Service 클래스
 * 
 * <p>
 * 고객 문의 등록, 답변 처리, 문의 내역 조회 등의 비즈니스 로직을 처리합니다.
 * 효율적인 고객 지원을 위한 문의응답(Q&A) 시스템을 관리합니다.
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 *   <li>고객 문의 등록 및 분류</li>
 *   <li>문의 내역 조회 및 검색</li>
 *   <li>ERP 직원의 답변 등록 및 수정</li>
 *   <li>문의 상태 관리 (접수/처리중/완료)</li>
 *   <li>문의 통계 및 리포트</li>
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
 *   <li>2025-06-26: 문의 로직 추가 (복성민)</li>
 * </ul>
 */

public interface InquiryService {

    public int insertQst(QuestionDTO questionDTO);

    public int insertAns(AnswerDTO answerDTO);
    
}
