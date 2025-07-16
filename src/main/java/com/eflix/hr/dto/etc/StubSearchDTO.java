package com.eflix.hr.dto.etc;

import java.util.Date;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class StubSearchDTO extends PagingDTO{
    private String empIdx;
    private Date date;

    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    public int getLimit() {
        return getPageUnit();
    }
}
