package com.eflix.main.dto;

import java.util.Date;

import lombok.Data;

// 최초 생성 6 28

@Data
public class InquiryDTO {
    private String qstIdx;
    private String qstTitle;
    private String qstWriter;
    private String qstContent;
    private String qstEmail;
    private String qstTel;
    private Date qstRegdate;
    private boolean answered; // 답변 여부 (true/false)
}