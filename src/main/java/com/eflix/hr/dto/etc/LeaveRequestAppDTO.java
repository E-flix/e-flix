package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class LeaveRequestAppDTO {
    private String leaveReqIdx;
    private String approverIdx;
    private String status;
    private String abReason;
}
