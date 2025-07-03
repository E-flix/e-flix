package com.eflix.bsn.service.impl;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrdersMapper ordersMapper;

    /*────────────────────── 조회 영역 ──────────────────────*/
    @Override
    @Transactional(readOnly = true)
    public List<OrdersDTO> getOrdersList() {
        return ordersMapper.selectAllOrders();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdersDTO getOrder(String orderNo) {
        OrdersDTO header = ordersMapper.selectOrderHeader(orderNo);
        header.setDetails(ordersMapper.selectOrderDetails(orderNo));
        return header;
    }

    /*────────────────────── 저장 영역 ──────────────────────*/
    @Override
    public String saveOrder(OrdersDTO dto) {

        boolean isNew = !StringUtils.hasText(dto.getOrderNo());

        /* 신규일 경우 주문번호 채번 */
        if (isNew) {
            dto.setOrderNo(generateNextOrderNo());
            ordersMapper.insertOrder(dto);
        } else {
            ordersMapper.updateOrder(dto);
            // 상세 전체 재작성
            ordersMapper.deleteOrderDetailAll(dto.getOrderNo());
        }

        /* 상세 라인번호 부여 */
        AtomicInteger seq = new AtomicInteger(1);
        dto.getDetails().forEach(d -> {
            d.setOrderNo(dto.getOrderNo());
            d.setLineNo(seq.getAndIncrement());
        });

        if (!dto.getDetails().isEmpty()) {
            ordersMapper.insertOrderDetailBatch(dto.getDetails());
        }
        return dto.getOrderNo();
    }

    /*────────────────────── 삭제 영역 ──────────────────────*/
    @Override
    public void deleteOrder(String orderNo) {
        ordersMapper.deleteOrder(orderNo);   // ON DELETE CASCADE
    }

    /*────────────────── 주문번호 채번 ───────────────────*/
    @Override
    @Transactional(readOnly = true)
    public String generateNextOrderNo() {
        return ordersMapper.selectNextOrderNo();
    }
}
