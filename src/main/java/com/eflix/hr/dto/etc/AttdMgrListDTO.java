package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class AttdMgrListDTO {
    private String empIdx;
    private String empName;
    private String deptName;
    private String grdName;
    private int totalDay;
    private int workDay;
    private int nightHours;
    private int lateCount;
    private int leaveEarlyCount;
    private int abCount;
    private int usedLeaveDay;
    private int lastModDate;
}
