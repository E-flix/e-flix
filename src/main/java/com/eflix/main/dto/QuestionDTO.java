package com.eflix.main.dto;

import java.util.Date;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 문의 정보를 전송하기 위한 DTO 클래스
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
public class QuestionDTO {
    private String qstIdx;      // qst-000
    private String qstTitle;
    private String qstWriter;
    private String qstContent;
    private Date qstRegdate;
    private String qstEmail;
    private String qstTel;
}
