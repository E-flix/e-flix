package com.eflix.main.dto.etc;

import com.eflix.common.paging.PagingDTO;

import lombok.Data;

@Data
public class InquirySearchDTO extends PagingDTO {
    private String qstIdx;
    private String qstWriter;
    private String qstEmail;
    private String qstTel;
    private String qstTitle;
    private String qstContent;
    private String qstRegdate;
    private Boolean answered;

    // offset 계산
    public int getOffset() {
        return (getPage() - 1) * getPageUnit();
    }

    // limit 가져오기
    public int getLimit() {
        return getPageUnit();
    }
}
