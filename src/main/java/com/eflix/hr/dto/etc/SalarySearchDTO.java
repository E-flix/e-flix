package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class SalarySearchDTO extends PagingDTO {
    private String coIdx;
    private String attMonth;
    private String empName;
    private String deptIdx;
    private String grdIdx;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
