package com.eflix.bsn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String customerCd;
    private String customerNm;
    private String representativeNm;
    private String phoneNo;
    private String salesEmpCd;
    private BigDecimal discountRate;

    private String businessRegNo;   // 사업자등록번호
    private String address;         // 주소
    private String email;           // 이메일
    private String creditGrade;     // 신용등급
    private String useYn;           // 사용여부 (Y/N)
    private LocalDate regDt;        // 등록일
    private LocalDate updDt;        // 수정일
    private String coIdx;           // 회사 식별자
}




