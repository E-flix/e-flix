package com.eflix.bsn.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.eflix.bsn.dto.*;
import com.eflix.bsn.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** ============================================
 *  작성자 : 이용진
 *  최초작성 : 2025-06-18
 *  설명   : 영업(BSN) 모듈 컨트롤러
 *  최신수정 : 2025-07-03 – 주문서 관리 단일 페이지 구조 적용
 * ============================================ */
@Slf4j
@Controller
@RequestMapping("/bsn")
@RequiredArgsConstructor
public class BsnController {

    /* ────────── 의존 서비스 ────────── */
    private final QuotationService quotationService;
    private final OrdersService    ordersService;
    private final CreditService    creditService;
    private final CustomerService  customerService;
    private final ItemService      itemService;
    private final ObjectMapper     objectMapper;   // (뷰에서 JSON 직렬화용)

    /*──────────── 1. 공통 메인 ────────────*/
    @GetMapping
    public String bsnMain() {
        return "bsn/bsnMain";
    }

    /*──────────────────────────────────────
     * 2. 견적서 영역
     *──────────────────────────────────────*/
    /** 견적서 목록 페이지 */
    @GetMapping("/qot_list")
    public String quotationList(Model model) {
        try {
            model.addAttribute("quotationList", quotationService.getQuotationList());
        } catch (Exception e) {
            log.error("견적서 조회 오류", e);
            model.addAttribute("quotationList", new ArrayList<>());
        }
        return "bsn/quotation_list";
    }

    /** 견적서 상세 품목(그리드) */
    @GetMapping("/quotation/details")
    @ResponseBody
    public List<QuotationDetailDTO> quotationDetails(@RequestParam String quotationNo) {
        try {
            return quotationService.getQuotationDetails(quotationNo);
        } catch (Exception e) {
            log.error("견적서 상세 조회 오류: {}", quotationNo, e);
            return new ArrayList<>();
        }
    }

    /** 견적서 입력 화면 */
    @GetMapping("/qot")
    public String quotationForm(Model model) {
        try {
            String nextNo = quotationService.generateNextQuotationNo();
            QuotationDTO dto = new QuotationDTO();
            dto.setQuotationNo(nextNo);
            dto.setDetails(List.of(new QuotationDetailDTO()));   // 한 줄 빈 Detail
            model.addAttribute("nextQuotationNo", nextNo);
            model.addAttribute("quotation", dto);
            return "bsn/quotation";
        } catch (Exception e) {
            log.error("견적서 입력 화면 오류", e);
            return "redirect:/bsn";
        }
    }

    /** 견적서 저장 */
    @PostMapping("/createQuotation")
    public String createQuotation(@ModelAttribute QuotationDTO quotation) {
        try {
            quotationService.createQuotation(quotation);
            return "redirect:/bsn/qot_list";
        } catch (Exception e) {
            log.error("견적서 저장 오류", e);
            return "redirect:/bsn/qot";
        }
    }

    /*──────────────────────────────────────
     * 3. 주문서(통합 관리) 영역
     *──────────────────────────────────────*/
    /** 주문서 통합 페이지(조회·등록·수정) */
    @GetMapping("/order_management")
    public String orderManagement() {
        return "bsn/salesorder_management";
    }

    /* ───── REST-API ───── */

    /** 주문 목록 (헤더 그리드) */
    @GetMapping("/orders")
    @ResponseBody
    public List<OrdersDTO> ordersList() {
        try {
            return ordersService.getOrdersList();
        } catch (Exception e) {
            log.error("주문 목록 조회 오류", e);
            return new ArrayList<>();
        }
    }

    /** 주문 단건(헤더+상세) */
    @GetMapping("/orders/{orderNo}")
    @ResponseBody
    public OrdersDTO orderDetail(@PathVariable String orderNo) {
        try {
            return ordersService.getOrder(orderNo);
        } catch (Exception e) {
            log.error("주문 단건 조회 오류: {}", orderNo, e);
            return new OrdersDTO();
        }
    }

    /** 주문번호 자동 채번 */
    @GetMapping("/orders/nextOrderNo")
    @ResponseBody
    public String nextOrderNo() {
        try {
            return ordersService.generateNextOrderNo();
        } catch (Exception e) {
            // 오류 시 오늘 날짜 기반 예비번호 리턴
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "SO-" + today + "-0001";
        }
    }

    /** 주문 저장(신규·수정) */
    @PostMapping("/orders")
    @ResponseBody
    public Map<String, Object> saveOrder(@RequestBody OrdersDTO dto) {
        Map<String, Object> res = new HashMap<>();
        try {
            String savedNo = ordersService.saveOrder(dto);
            res.put("success", true);
            res.put("orderNo", savedNo);
        } catch (Exception e) {
            log.error("주문 저장 오류", e);
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    /** 주문 삭제 */
    @DeleteMapping("/orders/{orderNo}")
    @ResponseBody
    public Map<String, Object> deleteOrder(@PathVariable String orderNo) {
        Map<String, Object> res = new HashMap<>();
        try {
            ordersService.deleteOrder(orderNo);
            res.put("success", true);
        } catch (Exception e) {
            log.error("주문 삭제 오류: {}", orderNo, e);
            res.put("success", false);
            res.put("message", e.getMessage());
        }
        return res;
    }

    /*──────────────────────────────────────
     * 4. 거래처 / 품목 / 여신  API
     *──────────────────────────────────────*/
    @GetMapping("/customer/info")
    @ResponseBody
    public CustomerDTO customerInfo(@RequestParam String customerCd) {
        try {
            return customerService.getCustomerInfo(customerCd);
        } catch (Exception e) {
            log.error("거래처 정보 조회 오류: {}", customerCd, e);
            return new CustomerDTO();
        }
    }

    @GetMapping("/customer/detail")
    @ResponseBody
    public Map<String, Object> customerWithCredit(@RequestParam String customerCd) {
        try {
            return customerService.getCustomerWithCredit(customerCd);
        } catch (Exception e) {
            log.error("거래처+여신 정보 조회 오류: {}", customerCd, e);
            return new HashMap<>();
        }
    }

    @GetMapping("/customer/search")
    @ResponseBody
    public List<CustomerDTO> customerSearch(@RequestParam String keyword) {
        try {
            return keyword != null && keyword.trim().length() >= 2
                   ? customerService.searchCustomers(keyword.trim())
                   : new ArrayList<>();
        } catch (Exception e) {
            log.error("거래처 검색 오류: {}", keyword, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/item/info")
    @ResponseBody
    public BsnItemDTO itemInfo(@RequestParam String code) {
        try {
            return Optional.ofNullable(itemService.getItemByCode(code))
                           .orElseGet(BsnItemDTO::new);
        } catch (Exception e) {
            log.error("품목 정보 조회 오류: {}", code, e);
            return new BsnItemDTO();
        }
    }

    @GetMapping("/item/search")
    @ResponseBody
    public List<BsnItemDTO> itemSearch(@RequestParam String keyword) {
        try {
            return keyword != null && keyword.trim().length() >= 2
                   ? itemService.searchItemsByName(keyword.trim())
                   : new ArrayList<>();
        } catch (Exception e) {
            log.error("품목 검색 오류: {}", keyword, e);
            return new ArrayList<>();
        }
    }

    /*──────────────────────────────────────
     * 5. 출고(임시) 뷰
     *──────────────────────────────────────*/
    @GetMapping("/obound_list")
    public String outboundList() {
        return "bsn/soutbound_list";
    }

    @GetMapping("/obound")
    public String outboundForm() {
        return "bsn/soutbound";
    }
}
