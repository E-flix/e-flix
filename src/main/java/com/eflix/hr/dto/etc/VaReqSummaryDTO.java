package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class VaReqSummaryDTO {
    private int total;
    private int pending;
    private int approved;
    private int rejected;
}
