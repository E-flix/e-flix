package com.eflix.bsn.mapper;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {

    /* ───── 조회 ───── */
    List<OrdersDTO> selectAllOrders(String coIdx);
    OrdersDTO      selectOrderHeader(OrdersDTO ordersDTO);
    List<OrdersDetailDTO> selectOrderDetails(OrdersDetailDTO ordersDetailDTO);

    /* ───── 채번 ───── */
    String selectNextOrderNo(String coIdx);

    String findLatestOrderNoForToday(@Param("prefix") String prefix, @Param("coIdx") String coIdx);


    /* ───── 저장 / 수정 ───── */
    int insertOrder(OrdersDTO dto);

    /* ───── 상세 일괄 INSERT ───── */
    int insertOrderDetailBatch(@Param("list") List<OrdersDetailDTO> list);

    /* ───── 수정 ───── */
    int updateOrder(OrdersDTO dto);
    int updateOrderDetail(OrdersDetailDTO detailDto);

    /* ───── 삭제 ───── */
    void deleteOrderDetailAll(String orderNo, String coIdx);   // ← 신규
    void deleteOrder(String orderNo, String coIdx);            // 기존
}
