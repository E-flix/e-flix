package com.eflix.hr.dto.etc;

import java.util.List;

import lombok.Data;

@Data
public class SalaryCalcDTO {
    private List<String> empIdxList;
    private String empIdx;
    private String date;
    private String coIdx;
}
