package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eflix.common.code.service.CommonService;
import com.eflix.purchs.dto.InboundDTO;
import com.eflix.purchs.service.InboundService;
import com.eflix.purchs.service.WarehouseService;
import lombok.RequiredArgsConstructor;
/** ============================================
  - 작성자   : 이혁진
  - 최초작성 : 2025-06-23
  - 설명     : 입고 기능 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-23 (이혁진): 입고화면 컨트롤러 완성
  - 2025-06-23 (이혁진): 파일명 ibound 수정 Ibound
============================================  */
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class InboundController {
  private final InboundService inboundService;
  private final CommonService commonService;
  private final WarehouseService warehouseService;

  // 입고조회
  @GetMapping("/ibd")
  public String inbound(Model model) {
    // 제품 - 반제품 조회 option
    model.addAttribute("ibdp", commonService.getCommon("PRD00"));
    // 제품조회
    model.addAttribute("ibdList", inboundService.getInbound());
    // 사용가능한 창고 조회
    model.addAttribute("vwh", warehouseService.warehouseState());
    return "purchs/inbound";
  }

  // 생산등록
  @PostMapping("/ibdp")
  @ResponseBody
  public InboundDTO prodInsert(@RequestBody InboundDTO inbound) {
    inboundService.insertProd(inbound);
    return inbound;
  }

  // 마지막 prod_id번호 가져오기
  @GetMapping("/lprd")
  @ResponseBody
  public String getNextProdId() {
    return inboundService.getNextProdId();
  }

  // 반품
  @DeleteMapping("/dprd")
  @ResponseBody
  public int prodDelete(@RequestBody InboundDTO inbound) {
    return inboundService.deleteProd(inbound);
  }

  // 마지막 inbound_id 가져오기
  @GetMapping("/gnpi")
  @ResponseBody
  public String getNextInboundId() {
    return inboundService.getNextInboundId();
  }

  // 마지막 inbound_lot 가져오기
  @GetMapping("/gnpl")
  @ResponseBody
  public String getNextInboundLot() {
    return inboundService.getNextInboundLot();
  }

  // 입고등록 (제품 -> 창고)
  @PostMapping("/pibd")
  @ResponseBody
  public InboundDTO inbound(@RequestBody InboundDTO inbound) {
    System.out.println(inbound.toString());
    inboundService.prodToWarehouse(inbound);
      return inbound;
  }
 }

