package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class SalaryMappingSearchDTO extends PagingDTO {
    private String option;
    private String keyword;
    private String type;
    private String useStatus;

    private String coIdx;

        public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
