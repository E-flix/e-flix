package com.eflix.hr.dto.etc;

import java.sql.Date;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class EmpSearchDTO extends PagingDTO {
    private String empIdx;           // emp_idx
    private String deptIdx;          // dept_idx
    private String empName;          // emp_name
    private String empEmail;         // emp_email
    private Date empRegdate;         // emp_regdate
    private String empGrade;         // emp_grade
    private String empType;          // emp_type
    private String empStatus;        // emp_status
    private String deptName;         // deptName
    private String empAccount;      // emp_account
    private String empPw;           // emp_pw
    private String rrnPfx;
    private String rrnSfx;
    private String empPhone;
    private String empMemo;
    private String empAddr;
    private String empAddrDetail;
    private String empAddrZip;
    
    private String option;
    private String keyword;

    private String coIdx;

    private String grdIdx;
    private String roleId;
    private String workTypeId;
    private String empImg;
    private String empContract;
    private String bankCode;
    private int leaveAllDays;
    private int leaveDays;
    private String empRole;

    private int baseSalary;
    private String deptUpIdx;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
