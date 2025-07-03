package com.eflix.bsn.mapper;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper {

    /* ───── 조회 ───── */
    List<OrdersDTO> selectAllOrders();
    OrdersDTO      selectOrderHeader(String orderNo);
    List<OrdersDetailDTO> selectOrderDetails(String orderNo);

    /* ───── 채번 ───── */
    String selectNextOrderNo();

    /* ───── 저장 / 수정 ───── */
    int insertOrder(OrdersDTO dto);
    int updateOrder(OrdersDTO dto);

    /* ───── 상세 일괄 INSERT ───── */
    int insertOrderDetailBatch(@Param("list") List<OrdersDetailDTO> list);

    /* ───── 삭제 ───── */
    int deleteOrder(String orderNo);          // 헤더(ON DELETE CASCADE)
    int deleteOrderDetailAll(String orderNo); // 상세만
}
