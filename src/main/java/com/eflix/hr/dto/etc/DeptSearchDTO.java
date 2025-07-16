package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class DeptSearchDTO extends PagingDTO {
    private String deptIdx;
    private String deptName;
    private String coIdx;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
