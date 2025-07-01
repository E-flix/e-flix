package com.eflix.bsn.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.service.OrdersService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersMapper ordersMapper;

    @Override
    @Transactional
    public void createOrder(OrdersDTO order) {
        
        ordersMapper.insertOrder(order);

        List<OrdersDetailDTO> details = order.getDetails();
        for (int i = 0; i < details.size(); i++) {
            OrdersDetailDTO d = details.get(i);
            d.setOrderNo(order.getOrderNo());
            d.setLineNo(i + 1);
            ordersMapper.insertOrderDetail(d);
        }

        // 여신 사용액 누적 업데이트
        BigDecimal total = order.getOrderTotal() != null
            ? order.getOrderTotal() : BigDecimal.ZERO;
        ordersMapper.updateCreditUsed(order.getCustomerCd(), total);
    }

    @Override
    public List<OrdersDTO> getOrdersList() {
        return ordersMapper.selectAllOrders();
    }
}

