// package com.eflix.purchs.service.impl;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.eflix.purchs.dto.OutboundViewDTO;
// import com.eflix.purchs.dto.WarehouseViewDTO;
// import com.eflix.purchs.mapper.OutboundViewMapper;
// import com.eflix.purchs.service.OutboundViewService;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class OutboundViewServiceImpl implements OutboundViewService {
//     private final OutboundViewMapper mapper;

//     @Override
//     public List<OutboundViewDTO> outboundViewList(WarehouseViewDTO outboundId) {
//         return mapper.outboundViewList(outboundId);
//     }

//     @Override
//     public List<OutboundViewDTO> outboundViewDetailList(String outboundId) {
//         return mapper.outboundViewDetailList(outboundId);
//     }

//     @Override
//     public List<OutboundViewDTO> searchOutboundId() {
//         return mapper.searchOutboundId();
//     }

// }
