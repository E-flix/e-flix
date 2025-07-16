package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class GradeSearchDTO extends PagingDTO {
    private String option;
    private String keyword;

    private String grdIdx;
    private String coIdx;
    private Integer grdOrder;
    private Double grdAnnual;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
