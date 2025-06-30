package com.eflix.bsn.service;

import java.util.List;

import com.eflix.bsn.dto.OrdersDTO;

public interface OrdersService {
    void createOrder(OrdersDTO order);

    List<OrdersDTO> getOrdersList();
}
