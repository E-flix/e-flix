package com.eflix.bsn.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.eflix.bsn.dto.CustomerDTO;

@Mapper
public interface CustomerMapper {
    /** 전체 거래처 조회 */
    List<CustomerDTO> selectAll();

    /** 이름으로 거래처 검색 (like 처리) */
    List<CustomerDTO> selectByName(@Param("name") String name);

    CustomerDTO selectByCustomerCd(String customerCd);

    Map<String, Object> selectCustomerWithCredit(String customerCd);

    List<CustomerDTO> searchCustomers(String keyword);

    List<CustomerDTO> searchByName(@Param("name") String name);

    List<Map<String, Object>> selectAllWithCreditSummary(@Param("coIdx") String coIdx);
    String findLatestCustomerCd(@Param("prefix") String prefix, @Param("coIdx") String coIdx);
    int insertCustomer(CustomerDTO dto);
    int updateCustomer(CustomerDTO dto);
    Integer findMaxCustomerSequence(@Param("prefix") String prefix);

    int softDeleteCustomer(@Param("customerCd") String customerCd, @Param("coIdx") String coIdx);
}
