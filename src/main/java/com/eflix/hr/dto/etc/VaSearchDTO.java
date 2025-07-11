package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class VaSearchDTO {
    private String reqType;
    private String approvalStatus;
    private String reqStart;
    private String reqEnd;

    private String coIdx;
}
