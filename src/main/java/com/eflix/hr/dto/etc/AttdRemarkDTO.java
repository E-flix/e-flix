package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class AttdRemarkDTO {
    private String attdIdx;
    private String empIdx;
    private String attdDate;
    private String attdStatus;
    private String startTime;
    private String endTime;
    private String abReason;
    private String memo;
    private String coIdx;
}
