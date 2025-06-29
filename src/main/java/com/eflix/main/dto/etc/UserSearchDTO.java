package com.eflix.main.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

// 최초 생성 6-27

@Data
public class UserSearchDTO extends PagingDTO {
    private String userId;
    private String userEmail;
    private String userTel;

    // offset 계산
    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    // limit 가져오기
    public int getLimit() {
        return getPageUnit();
    }
}