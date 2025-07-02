package com.eflix.bsn.controller;

import java.time.LocalDate;  
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.CustomerService;
import com.eflix.bsn.service.ItemService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.bsn.service.QuotationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** ============================================
  - 작성자   : 이용진
  - 최초작성 : 2025-06-18
  - 설명     : 영업 컨트롤러
============================================  */
@Slf4j
@Controller
@RequestMapping("/bsn")
@RequiredArgsConstructor
public class BsnController {

  private final QuotationService quotationService;
  private final OrdersService ordersService;
  private final CreditService creditService;
  private final ObjectMapper    objectMapper;
  private final CustomerService customerService;
  private final ItemService itemService; 

  //영업 관리 메인 
  @GetMapping()
  public String bsnMain(){
    return "bsn/bsnMain";
  }

  // ========== 견적서 관련 메서드들 ==========
  
  //견적서 조회
  @GetMapping("/qot_list")
  public String quotation_list(Model model) {
    try {
      var list = quotationService.getQuotationList();
      model.addAttribute("quotationList", list);
      return "bsn/quotation_list";
    } catch (Exception e) {
      log.error("견적서 조회 중 오류 발생", e);
      model.addAttribute("quotationList", new ArrayList<>());
      return "bsn/quotation_list";
    }
  }

  /** 견적서 상세(품목) 목록 */
  @GetMapping("/quotation/details")
  @ResponseBody
  public List<QuotationDetailDTO> getQuotationDetails(
      @RequestParam("quotationNo") String quotationNo) {
    try {
      return quotationService.getQuotationDetails(quotationNo);
    } catch (Exception e) {
      log.error("견적서 상세 조회 중 오류 발생: quotationNo = {}", quotationNo, e);
      return new ArrayList<>();
    }
  }

  //견적서 입력
  @GetMapping("/qot")
  public String quotation(Model model) {
    try {
      // 1) 다음 견적번호
      String nextNo = quotationService.generateNextQuotationNo();
      model.addAttribute("nextQuotationNo", nextNo);

      // 2) 빈 DTO + 한 건의 Detail 초기화
      QuotationDTO dto = new QuotationDTO();
      dto.setQuotationNo(nextNo);
      dto.setDetails(new ArrayList<>());
      dto.getDetails().add(new QuotationDetailDTO());

      model.addAttribute("quotation", dto);
      return "bsn/quotation";
    } catch (Exception e) {
      log.error("견적서 입력 화면 로딩 중 오류 발생", e);
      return "redirect:/bsn";
    }
  }

  /** 견적서 등록 처리 */
  @PostMapping("/createQuotation")
  public String createQuotation(@ModelAttribute QuotationDTO quotation) {
    try {
      quotationService.createQuotation(quotation);
      return "redirect:/bsn/qot_list";  // 등록 후 목록으로
    } catch (Exception e) {
      log.error("견적서 등록 중 오류 발생", e);
      return "redirect:/bsn/qot";
    }
  }

  // ========== 주문서 관련 메서드들 (NEW) ==========
  
  // 🆕 통합 주문서 관리 페이지
  @GetMapping("/order_management")
  public String orderManagement(Model model) {
    try {
      log.info("주문서 관리 페이지 접속");
      return "bsn/salesorder_management";
    } catch (Exception e) {
      log.error("주문서 관리 페이지 로딩 중 오류 발생", e);
      return "redirect:/bsn";
    }
  }

  // 🔄 기존 주문서 조회 메서드 수정 (리다이렉트)
  @GetMapping("/sorlist")
  public String salesorder_list_redirect() {
    log.info("기존 주문서 조회 페이지 -> 새로운 관리 페이지로 리다이렉트");
    return "redirect:/bsn/order_management";
  }

  // 🔄 기존 주문서 등록 메서드 수정 (리다이렉트)  
  @GetMapping("/sorder")
  public String salesorder_redirect() {
    log.info("기존 주문서 등록 페이지 -> 새로운 관리 페이지로 리다이렉트");
    return "redirect:/bsn/order_management";
  }

  // 🆕 주문서 목록 데이터 제공 (AJAX용)
  @GetMapping("/sorlist/data")
  @ResponseBody
  public List<OrdersDTO> getOrdersListData() {
    try {
      log.info("주문서 목록 데이터 요청");
      return ordersService.getOrdersList();
    } catch (Exception e) {
      log.error("주문서 목록 데이터 조회 중 오류 발생", e);
      return new ArrayList<>();
    }
  }

  // 🆕 주문서 번호 자동 생성 API
  @GetMapping("/orders/nextOrderNo")
  @ResponseBody
  public String getNextOrderNo() {
    try {
      String nextNo = ordersService.generateNextOrderNo();
      log.info("주문서 번호 생성: {}", nextNo);
      return nextNo;
    } catch (Exception e) {
      log.error("주문서 번호 생성 중 오류 발생", e);
      // 임시 번호 반환
      String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
      return String.format("SO-%s-0001", today);
    }
  }

  // 🆕 주문서 JSON 저장 처리 (AJAX용)
  @PostMapping("/sorder/json")  
  @ResponseBody
  public Map<String, Object> createOrderJson(@RequestBody OrdersDTO order) {
    Map<String, Object> result = new HashMap<>();
    try {
      log.info("주문서 JSON 저장 요청: {}", order.getOrderNo());
      ordersService.createOrder(order);
      result.put("success", true);
      result.put("message", "주문서가 성공적으로 저장되었습니다.");
      result.put("orderNo", order.getOrderNo());
      log.info("주문서 저장 완료: {}", order.getOrderNo());
    } catch (Exception e) {
      log.error("주문서 JSON 저장 중 오류 발생", e);
      result.put("success", false);
      result.put("message", "저장 중 오류가 발생했습니다: " + e.getMessage());
    }
    return result;
  }

  // 🆕 주문서 삭제 API
  @DeleteMapping("/sorder/{orderNo}")
  @ResponseBody
  public Map<String, Object> deleteOrder(@PathVariable String orderNo) {
    Map<String, Object> result = new HashMap<>();
    try {
      log.info("주문서 삭제 요청: {}", orderNo);
      ordersService.deleteOrder(orderNo);
      result.put("success", true);
      result.put("message", "주문서가 성공적으로 삭제되었습니다.");
      log.info("주문서 삭제 완료: {}", orderNo);
    } catch (Exception e) {
      log.error("주문서 삭제 중 오류 발생: orderNo = {}", orderNo, e);
      result.put("success", false);
      result.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
    }
    return result;
  }

  // ========== 기존 주문서 관련 메서드들 (DEPRECATED) ==========
  
  // 주문서 등록 화면 (사용 안함 - 리다이렉트로 대체)
  /* 
  @GetMapping("/sorder")
  public String salesorder(@RequestParam(value="customerCd", required=false) String customerCd,
                          Model model) {
    // 생략 - 새로운 관리 페이지로 리다이렉트 처리됨
  }
  */

  // 주문서 저장 처리 (사용 안함 - JSON API로 대체)
  /*
  @PostMapping("/sorder")
  public String createOrder(@ModelAttribute OrdersDTO order) {
    // 생략 - JSON API로 대체됨
  }
  */

  // ========== 거래처/품목 관련 메서드들 ==========

  /** 거래처 정보 조회 API */
  @GetMapping("/customer/info")
  @ResponseBody
  public CustomerDTO getCustomerInfo(@RequestParam("customerCd") String customerCd) {
    try {
      return customerService.getCustomerInfo(customerCd);
    } catch (Exception e) {
      log.error("거래처 정보 조회 중 오류 발생: customerCd = {}", customerCd, e);
      return new CustomerDTO(); // 빈 객체 반환
    }
  }

  /** 거래처 + 여신정보 통합 조회 API */
  @GetMapping("/customer/detail")
  @ResponseBody
  public Map<String, Object> getCustomerWithCredit(@RequestParam("customerCd") String customerCd) {
    try {
      return customerService.getCustomerWithCredit(customerCd);
    } catch (Exception e) {
      log.error("거래처 상세정보 조회 중 오류 발생: customerCd = {}", customerCd, e);
      return new HashMap<>();
    }
  }
  
  /** 거래처 검색 API (자동완성용) */
  @GetMapping("/customer/search")
  @ResponseBody
  public List<CustomerDTO> searchCustomers(@RequestParam("keyword") String keyword) {
    try {
      if (keyword == null || keyword.trim().length() < 2) {
        return new ArrayList<>();
      }
      return customerService.searchCustomers(keyword.trim());
    } catch (Exception e) {
      log.error("거래처 검색 중 오류 발생: keyword = {}", keyword, e);
      return new ArrayList<>();
    }
  }
  
  /** 품목 정보 조회 API */
  @GetMapping("/item/info")
  @ResponseBody
  public BsnItemDTO getItemInfo(@RequestParam("code") String itemCode) {
    try {
      BsnItemDTO item = itemService.getItemByCode(itemCode);
      if (item == null) {
        log.warn("품목 정보를 찾을 수 없습니다: {}", itemCode);
        return new BsnItemDTO(); // 빈 객체 반환
      }
      return item;
    } catch (Exception e) {
      log.error("품목 정보 조회 중 오류 발생: itemCode = {}", itemCode, e);
      return new BsnItemDTO(); // 빈 객체 반환
    }
  }

  /** 품목 검색 API (자동완성용) */
  @GetMapping("/item/search")
  @ResponseBody
  public List<BsnItemDTO> searchItems(@RequestParam("keyword") String keyword) {
    try {
      if (keyword == null || keyword.trim().length() < 2) {
        return new ArrayList<>();
      }
      return itemService.searchItemsByName(keyword.trim());
    } catch (Exception e) {
      log.error("품목 검색 중 오류 발생: keyword = {}", keyword, e);
      return new ArrayList<>();
    }
  }

  // ========== 출고 관련 메서드들 ==========

  //출고 조회
  @GetMapping("/obound_list")
  public String outbound_list(){
    return "bsn/soutbound_list";
  }

  //출고 의뢰
  @GetMapping("/obound")
  public String outbound(){
    return "bsn/soutbound";
  }
}