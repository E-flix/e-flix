package com.eflix.main.dto.etc;

import java.util.Date;

import lombok.Data;

@Data
public class InquirySummaryDTO {
    private String ansrIdx;
    private String qstIdx;
    private String ansrTitle;
    private Date ansrRegdate;
}
