package com.eflix.common.paging;

import lombok.Data;

// 최초 생성 06 27

@Data
public class PagingDTO {
    int pageUnit = 10;    // 한 페이지에 보여줄 레코드 수
    int pageSize = 10;    // 페이징 그룹 수 (예: 1~10)
    int lastPage;
    int totalRecord;
    Integer page = 1;
    int startPage;
    int endPage;
    int first;
    int last;

    public int getFirst() {
        first = (getPage() - 1) * getPageUnit() + 1;
        return first;
    }

    public int getLast() {
        last = getPage() * getPageUnit();
        return last;
    }

    public int getLastPage() {
        lastPage = totalRecord / pageUnit + (totalRecord % pageUnit > 0 ? 1 : 0);
        return lastPage;
    }

    public int getStartPage() {
        startPage = (page - 1) / pageSize * pageSize + 1;
        return startPage;
    }

    public int getEndPage() {
        endPage = (page - 1) / pageSize * pageSize + pageSize;
        if (endPage > getLastPage()) {
            endPage = getLastPage();
        }
        return endPage;
    }
}
