package com.eflix.hr.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class VaSearchDTO extends PagingDTO{
    private String reqType;
    private String approvalStatus;
    private String reqStart;
    private String reqEnd;

    private String coIdx;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
