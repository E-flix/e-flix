package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class AttdSearchDTO extends PagingDTO {
    private String empName;
    private String empStatus;
    private String deptIdx;
    private String dateYear;
    private String dateMonth;
    private String grdIdx;
    private String date;
    private String attdStatus;
    private String coIdx;

    private String deptUpIdx;
    private String deptDownIdx;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
