package com.eflix.bsn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.service.OrdersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersMapper ordersMapper;

    @Override
    public List<OrdersDTO> getOrdersList() {
        return ordersMapper.getOrdersList();
    }
}
