package com.eflix.hr.dto.etc;

import java.util.Date;

import lombok.Data;

@Data
public class AttdRemarkDTO {
    private String attdIdx;
    private String empIdx;
    private String attdDate;
    private String attdStatus;
    private String abReason;
    private String memo;
    private String coIdx;
}
