package com.eflix.bsn.dto;

import java.math.BigDecimal;

public class CustomerDTO {
    private String customerCd;
    private String customerNm;
    private String representativeNm;
    private String phoneNo;
    private BigDecimal discountRate;

    // (필요하다면 대표자, 주소 등 다른 필드도 추가)

    // 기본 생성자
    public CustomerDTO() {}

    // getters & setters
    public String getCustomerCd() { return customerCd; }
    public void setCustomerCd(String customerCd) { this.customerCd = customerCd; }

    public String getCustomerNm() { return customerNm; }
    public void setCustomerNm(String customerNm) { this.customerNm = customerNm; }

    // public String getSalesEmpCd() { return salesEmpCd; }
    // public void setSalesEmpCd(String salesEmpCd) { this.salesEmpCd = salesEmpCd; }

    public String getRepresentativeNm(){
        return representativeNm;
    }

    public void setRepresentativeNM(String representativeNm){
        this.representativeNm = representativeNm;
    }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}
