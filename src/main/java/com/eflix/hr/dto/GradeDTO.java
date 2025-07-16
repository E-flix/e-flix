package com.eflix.hr.dto;

import lombok.Data;

@Data
public class GradeDTO {
    private String grdIdx;
    private String grdName;
    private Integer grdOrder;
    private Double grdAnnual;
    private String coIdx;

    private Integer empCount;
}