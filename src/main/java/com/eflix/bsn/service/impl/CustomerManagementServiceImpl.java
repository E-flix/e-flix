package com.eflix.bsn.service.impl;

import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.dto.CustomerWithCreditDTO;
import com.eflix.bsn.mapper.CreditMapper;
import com.eflix.bsn.mapper.CustomerMapper;
import com.eflix.bsn.service.CustomerManagementService;
import com.eflix.common.security.auth.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerManagementServiceImpl implements CustomerManagementService {

    private final CustomerMapper customerMapper;
    private final CreditMapper creditMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCustomerListWithCreditSummary() {
        String coIdx = AuthUtil.getCoIdx();
        return customerMapper.selectAllWithCreditSummary(coIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerWithCreditDTO getCustomerWithCredit(String customerCd) {
        CustomerDTO customer = customerMapper.selectByCustomerCd(customerCd);
        if (customer == null) return null;

        CreditInfoDTO creditInfo = creditMapper.selectByCustomerCd(customerCd);
        
        CustomerWithCreditDTO response = new CustomerWithCreditDTO();
        response.setCustomer(customer);
        response.setCreditInfo(creditInfo != null ? creditInfo : new CreditInfoDTO());
        
        return response;
    }

    @Override
    @Transactional
    public Map<String, Object> saveCustomerWithCredit(CustomerWithCreditDTO dto) {
        CustomerDTO customer = dto.getCustomer();
        CreditInfoDTO creditInfo = dto.getCreditInfo();
        String coIdx = AuthUtil.getCoIdx();

        boolean isNew = !StringUtils.hasText(customer.getCustomerCd());

        if (isNew) {
            // 신규 등록
            String newCustomerCd = generateNextCustomerCd();
            customer.setCustomerCd(newCustomerCd);
            customer.setCoIdx(coIdx);
            customer.setRegDt(LocalDate.now());
            customer.setUseYn("Y");
            
            creditInfo.setCustomerCd(newCustomerCd);
            creditInfo.setCoIdx(coIdx);

            log.info("신규 거래처 등록: {}", newCustomerCd);
            customerMapper.insertCustomer(customer);
            // MERGE 쿼리를 사용하므로 updateCreditInfo로 통일
            creditMapper.updateCreditInfo(creditInfo);

        } else {
            // 기존 정보 수정
            customer.setCoIdx(coIdx);
            customer.setUpdDt(LocalDate.now());
            
            // [수정] creditInfo 객체에 customerCd를 설정하는 코드 추가
            creditInfo.setCustomerCd(customer.getCustomerCd());
            creditInfo.setCoIdx(coIdx);

            log.info("거래처 정보 수정: {}", customer.getCustomerCd());
            customerMapper.updateCustomer(customer);
            creditMapper.updateCreditInfo(creditInfo);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("customerCd", customer.getCustomerCd());
        result.put("message", "거래처 정보가 성공적으로 저장되었습니다.");
        
        return result;
    }


    @Override
    @Transactional
    public void deleteCustomer(String customerCd) {
        String coIdx = AuthUtil.getCoIdx();
        log.info("거래처 비활성화 요청: coIdx={}, customerCd={}", coIdx, customerCd);

        int updatedRows = customerMapper.softDeleteCustomer(customerCd, coIdx);
        if (updatedRows == 0) {
            throw new RuntimeException("삭제할 거래처를 찾을 수 없거나 권한이 없습니다.");
        }
        log.info("거래처 비활성화 완료: {}", customerCd);
    }

    private synchronized String generateNextCustomerCd() {
        String prefix = "CUS";
        // 데이터베이스에서 'cus'로 시작하는 코드 중 가장 큰 숫자 부분을 조회합니다.
        Integer maxSequence = customerMapper.findMaxCustomerSequence(prefix);
        
        // 다음 번호는 (가장 큰 번호 + 1) 입니다.
        int nextSequence = (maxSequence != null ? maxSequence : 0) + 1;
        
        // 'cus' + 4자리 숫자 형식으로 포맷팅하여 반환합니다.
        return prefix + String.format("%04d", nextSequence);
    }



}
