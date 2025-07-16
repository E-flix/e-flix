package com.eflix.bsn.service;

import com.eflix.bsn.dto.CustomerWithCreditDTO;
import java.util.List;
import java.util.Map;

public interface CustomerManagementService {

    /**
     * 전체 거래처 목록을 여신 요약 정보와 함께 조회합니다.
     */
    List<Map<String, Object>> getCustomerListWithCreditSummary();

    /**
     * 특정 거래처의 상세 정보와 여신 정보를 함께 조회합니다.
     */
    CustomerWithCreditDTO getCustomerWithCredit(String customerCd);

    /**
     * 거래처와 여신 정보를 함께 저장하거나 수정합니다.
     * @return 저장된 거래처 정보와 성공 여부를 담은 Map
     */
    Map<String, Object> saveCustomerWithCredit(CustomerWithCreditDTO dto);

    void deleteCustomer(String customerCd);
}
