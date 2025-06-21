package com.eflix.bsn.mapper;

import java.util.List;

import com.eflix.bsn.dto.OrdersDTO;

/**
 * 주문서 Mapper
 */
public interface OrdersMapper {
    List<OrdersDTO> getOrdersList();
}
