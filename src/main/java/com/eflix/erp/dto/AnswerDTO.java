package com.eflix.erp.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 답변 정보를 전송하기 위한 DTO 클래스
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
public class AnswerDTO {
    private String ansrIdx;
    private String qstIdx;
    private String empIdx;
    private String ansrTitle;
    private String ansrContent;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ansrRegdate;
}