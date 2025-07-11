package com.eflix.hr.dto.etc;

import java.util.Date;

import com.eflix.common.paging.PagingDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class VaDTO extends PagingDTO {
    private String leaveReqIdx;      // leave_req_idx
    private String empIdx;           // emp_idx
    private String approverIdx;      // approver_idx
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date leaveStartDate; // leave_start_date
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date leaveEndDate;   // leave_end_date
    private String reason;           // reason
    private Date requestDate;   // request_date
    private String approvalStatus;   // approval_status
    private Date approvedDate;  // approved_date
    private String attFile;          // att_file

    private String reqType;
    private String common_code_name;
    private String coIdx;

    private String empImg;
    private String empName;
    private String deptName;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
