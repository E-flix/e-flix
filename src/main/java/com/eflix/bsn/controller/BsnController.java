package com.eflix.bsn.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.CustomerService;
import com.eflix.bsn.service.ItemService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.bsn.service.QuotationService;
import com.eflix.bsn.service.SOutboundService;
import com.eflix.common.security.auth.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** ============================================
 *  영업(BSN) 모듈 컨트롤러
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
    private final SOutboundService sOutboundService;
    private final ObjectMapper     objectMapper;

    /*──────────── 1. 메인 ────────────*/
    @GetMapping
    public String bsnMain() { return "bsn/bsnMain"; }

    /*──────────────────────────────
     * 2. 견적서 영역
     *──────────────────────────────*/
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

    /**
     * ★ 견적서 목록 JSON API (주문서 변환용) - 디버깅 강화
     */
    @GetMapping("/quotation/list")
    @ResponseBody
    public List<QuotationDTO> quotationListJson() {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 목록 JSON 조회 시작 - 회사: {}", coIdx);
            
            List<QuotationDTO> quotations = quotationService.getQuotationList();
            
            log.info("견적서 목록 조회 완료 - 회사: {}, 건수: {}", coIdx, quotations.size());
            
            // ★ 첫 번째 견적서 로그 (디버깅용)
            if (!quotations.isEmpty()) {
                QuotationDTO first = quotations.get(0);
                log.info("첫 번째 견적서 샘플: quotationNo={}, customerName={}, customerCd={}", 
                        first.getQuotationNo(), first.getCustomerName(), first.getCustomerCd());
            }
            
            return quotations;
        } catch (Exception e) {
            log.error("견적서 목록 JSON 조회 오류 - 회사: {}", coIdx, e);
            return new ArrayList<>();
        }
    }

    /**
     * ★ 견적서 상세 조회 (통합된 최종 버전)
     */
    @GetMapping("/quotation/details")
    @ResponseBody
    public List<QuotationDetailDTO> quotationDetails(@RequestParam String quotationNo) {
        String coIdx = AuthUtil.getCoIdx();
        try {
            log.info("견적서 상세 조회 시작 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            
            List<QuotationDetailDTO> details = quotationService.getQuotationDetails(quotationNo);
            
            log.info("견적서 상세 조회 완료 - 회사: {}, 견적서: {}, 상세건수: {}", 
                    coIdx, quotationNo, details.size());
            
            // ★ 첫 번째 상세 로그 (디버깅용)
            if (!details.isEmpty()) {
                QuotationDetailDTO first = details.get(0);
                log.info("첫 번째 상세 샘플: itemCode={}, itemName={}, qty={}, unitPrice={}", 
                        first.getItemCode(), first.getItemName(), first.getQty(), first.getUnitPrice());
            }
            
            return details;
        } catch (Exception e) {
            log.error("견적서 상세 조회 오류 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            return new ArrayList<>();
        }
    }

    /**
     * ★ 견적서 목록 필터링 API
     */
    @GetMapping("/quotation/search")
    @ResponseBody
    public List<QuotationDTO> searchQuotations(
        @RequestParam(required = false) String quotationNo,
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String dateFrom,
        @RequestParam(required = false) String dateTo
    ) {
        try {
            List<QuotationDTO> allQuotations = quotationService.getQuotationList();
            
            return allQuotations.stream()
                .filter(q -> quotationNo == null || quotationNo.trim().isEmpty() 
                    || (q.getQuotationNo() != null && q.getQuotationNo().contains(quotationNo.trim())))
                .filter(q -> customerName == null || customerName.trim().isEmpty() 
                    || (q.getCustomerName() != null && q.getCustomerName().contains(customerName.trim())))
                .filter(q -> {
                    if (dateFrom == null || dateFrom.trim().isEmpty()) return true;
                    if (q.getQuotationDt() == null) return false;
                    try {
                        String qDateStr = q.getQuotationDt().toString().substring(0, 10);
                        return qDateStr.compareTo(dateFrom) >= 0;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .filter(q -> {
                    if (dateTo == null || dateTo.trim().isEmpty()) return true;
                    if (q.getQuotationDt() == null) return false;
                    try {
                        String qDateStr = q.getQuotationDt().toString().substring(0, 10);
                        return qDateStr.compareTo(dateTo) <= 0;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
        } catch (Exception e) {
            log.error("견적서 검색 오류", e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/qot")
    public String quotationForm(Model model) {
        try {
            String nextNo = quotationService.generateNextQuotationNo();
            QuotationDTO dto = new QuotationDTO();
            dto.setQuotationNo(nextNo);
            dto.setDetails(List.of(new QuotationDetailDTO()));
            model.addAttribute("nextQuotationNo", nextNo);
            model.addAttribute("quotation", dto);
            return "bsn/quotation";
        } catch (Exception e) {
            log.error("견적서 입력 화면 오류", e);
            return "redirect:/bsn";
        }
    }

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

    /*──────────────────────────────
     * 3. 주문서 영역
     *──────────────────────────────*/
    @GetMapping("/order_management")
    public String orderManagement() { return "bsn/salesorder_management"; }

    /* ───── REST: 헤더 목록 ───── */
    /** ✅ 헤더 그리드(신규) */
    @GetMapping("/orders")
    @ResponseBody
    public List<OrdersDTO> ordersList() {
        try { return ordersService.getOrdersList(); }
        catch (Exception e) {
            log.error("주문 목록 조회 오류", e);
            return new ArrayList<>();
        }
    }
    /** ✅ 헤더 그리드(legacy 경로) */
    @GetMapping("/sorlist/data")
    @ResponseBody
    public List<OrdersDTO> ordersListLegacy() {
        return ordersList();   // 동일 서비스 호출
    }

    /* ───── REST: 단건 헤더 ───── */
    @GetMapping("/orders/{orderNo}")
    @ResponseBody
    public OrdersDTO orderHeader(@PathVariable String orderNo) {
        try { return ordersService.getOrder(orderNo); }
        catch (Exception e) {
            log.error("주문 헤더 조회 오류: {}", orderNo, e);
            return new OrdersDTO();
        }
    }

    /* ───── REST: 디테일 목록 ───── */
    @GetMapping("/orders/{orderNo}/details")
    @ResponseBody
    public List<OrdersDetailDTO> orderDetails(@PathVariable String orderNo) {
        try { return ordersService.getOrderDetails(orderNo); }
        catch (Exception e) {
            log.error("주문 디테일 조회 오류: {}", orderNo, e);
            return new ArrayList<>();
        }
    }

    /* ───── 번호 채번 ───── */
    @GetMapping("/orders/nextOrderNo")
    @ResponseBody
    public String nextOrderNo() {
        try { return ordersService.generateNextOrderNo(); }
        catch (Exception e) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "SO-" + today + "-0001";
        }
    }

    /* ───── 저장 / 삭제 (멀티테넌트 지원) ───── */
    @PostMapping("/orders")
    @ResponseBody
    public Map<String,Object> saveOrder(@RequestBody OrdersDTO dto) {
        Map<String,Object> res = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        
        try {
            log.info("주문서 저장 요청 - 회사: {}, 사원: {}, 주문번호: {}", 
                    coIdx, empIdx, dto.getOrderNo());
            
            // ★ 요청 데이터 로깅 (디버깅용)
            log.info("요청 헤더: customerCd={}, orderWriter={}, orderDt={}", 
                    dto.getCustomerCd(), dto.getOrderWriter(), dto.getOrderDt());
            log.info("요청 디테일: {} 건", dto.getDetails() != null ? dto.getDetails().size() : 0);
            
            String orderNo = ordersService.saveOrder(dto);
            
            res.put("orderNo", orderNo);
            res.put("success", true);
            res.put("message", "주문서가 성공적으로 저장되었습니다.");
            
            log.info("주문서 저장 성공 - 회사: {}, 주문번호: {}", coIdx, orderNo);
            
        } catch (IllegalArgumentException e) {
            // ★ 유효성 검증 오류
            log.warn("주문서 저장 실패 (유효성 검증) - 회사: {}, 오류: {}", coIdx, e.getMessage());
            res.put("success", false);
            res.put("message", "입력 오류: " + e.getMessage());
            res.put("errorType", "VALIDATION_ERROR");
            
        } catch (RuntimeException e) {
            // ★ 비즈니스 로직 오류
            log.error("주문서 저장 실패 (비즈니스 로직) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "저장 오류: " + e.getMessage());
            res.put("errorType", "BUSINESS_ERROR");
            
        } catch (Exception e) {
            // ★ 시스템 오류
            log.error("주문서 저장 실패 (시스템 오류) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "시스템 오류가 발생했습니다. 관리자에게 문의하세요.");
            res.put("errorType", "SYSTEM_ERROR");
            res.put("errorDetail", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        
        return res;
    }

    @DeleteMapping("/orders/{orderNo}")
    @ResponseBody
    public Map<String,Object> deleteOrder(@PathVariable String orderNo) {
        Map<String,Object> res = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("주문서 삭제 요청 - 회사: {}, 주문번호: {}", coIdx, orderNo);
            
            ordersService.deleteOrder(orderNo);
            
            res.put("success", true);
            res.put("message", "주문서가 성공적으로 삭제되었습니다.");
            
            log.info("주문서 삭제 성공 - 회사: {}, 주문번호: {}", coIdx, orderNo);
            
        } catch (Exception e) {
            log.error("주문서 삭제 실패 - 회사: {}, 주문번호: {}, 오류: {}", coIdx, orderNo, e.getMessage(), e);
            res.put("success", false);
            res.put("message", "삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return res;
    }

    /*──────────────────────────────
     * 4. 거래처 / 품목 / 여신 (멀티테넌트 지원)
     *──────────────────────────────*/
    @GetMapping("/customer/info")
    @ResponseBody
    public CustomerDTO customerInfo(@RequestParam String customerCd){
        try { 
            // ★ 회사별 거래처 정보 조회 시 추가 보안 검증 가능
            return customerService.getCustomerInfo(customerCd); 
        }
        catch(Exception e){ 
            log.error("거래처 정보 오류 - 회사: {}, 거래처: {}", 
                AuthUtil.getCoIdx(), customerCd, e); 
            return new CustomerDTO(); 
        }
    }

    /**
     * ★ 견적서 등록용: 거래처 목록/검색 API
     */
    @GetMapping("/customer/list")
    @ResponseBody
    public List<CustomerDTO> customerList(@RequestParam(required = false) String customerName) {
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("거래처 목록 조회 - 회사: {}, 검색어: {}", coIdx, customerName);
            
            if (customerName != null && !customerName.trim().isEmpty()) {
                return customerService.searchCustomersByName(customerName.trim());
            } else {
                return customerService.findAll();
            }
        } catch (Exception e) {
            log.error("거래처 목록 조회 오류 - 회사: {}, 검색어: {}", 
                    AuthUtil.getCoIdx(), customerName, e);
            return new ArrayList<>();
        }
    }

    @GetMapping("/customer/detail")
    @ResponseBody
    public Map<String,Object> customerWithCredit(@RequestParam String customerCd){
        try { 
            String coIdx = AuthUtil.getCoIdx();
            log.info("거래처+여신 조회 - 회사: {}, 거래처: {}", coIdx, customerCd);
            return customerService.getCustomerWithCredit(customerCd); 
        }
        catch(Exception e){ 
            log.error("거래처+여신 오류 - 회사: {}, 거래처: {}", 
                AuthUtil.getCoIdx(), customerCd, e); 
            return new HashMap<>(); 
        }
    }

    @GetMapping("/customer/{customerCd}/credit")
    @ResponseBody
    public CreditInfoDTO creditInfo(@PathVariable String customerCd){
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("여신 정보 조회 - 회사: {}, 거래처: {}", coIdx, customerCd);
            return creditService.getCreditInfo(customerCd);
        } catch(Exception e) {
            log.error("여신 정보 오류 - 회사: {}, 거래처: {}", 
                AuthUtil.getCoIdx(), customerCd, e);
            // ★ 기본 여신 정보 반환
            CreditInfoDTO defaultCredit = new CreditInfoDTO();
            defaultCredit.setCustomerCd(customerCd);
            defaultCredit.setCreditLimit(java.math.BigDecimal.ZERO);
            defaultCredit.setCreditUsed(java.math.BigDecimal.ZERO);
            defaultCredit.setCreditStatus("정상");
            return defaultCredit;
        }
    }

    @GetMapping("/customer/search")
    @ResponseBody
    public List<CustomerDTO> customerSearch(@RequestParam String keyword){
        try {
            return keyword!=null && keyword.trim().length()>=2
                ? customerService.searchCustomers(keyword.trim())
                : new ArrayList<>();
        } catch(Exception e){
            log.error("거래처 검색 오류: {}", keyword, e); return new ArrayList<>();
        }
    }

    /**
     * ★ 주문서 관리용: 거래처 목록 API (별칭)
     */
    @GetMapping("/customers/search")
    @ResponseBody
    public List<CustomerDTO> customers(@RequestParam(required = false) String name) {
        return customerList(name); // 위의 customerList 메서드 재사용
    }

    @GetMapping("/item/info")
    @ResponseBody
    public BsnItemDTO itemInfo(@RequestParam String code){
        try { return Optional.ofNullable(itemService.getItemByCode(code)).orElseGet(BsnItemDTO::new); }
        catch(Exception e){ log.error("품목 정보 오류: {}", code, e); return new BsnItemDTO(); }
    }

    /**
     * ★ 견적서 등록용: 품목 목록/검색 API
     */
    @GetMapping("/item/list")
    @ResponseBody
    public List<BsnItemDTO> itemList(@RequestParam(required = false) String itemName) {
        try {
            String coIdx = AuthUtil.getCoIdx();
            log.info("품목 목록 조회 - 회사: {}, 검색어: {}", coIdx, itemName);
            
            if (itemName != null && !itemName.trim().isEmpty()) {
                return itemService.searchItemsByName(itemName.trim());
            } else {
                return itemService.getAllItems();
            }
        } catch (Exception e) {
            log.error("품목 목록 조회 오류 - 회사: {}, 검색어: {}", 
                    AuthUtil.getCoIdx(), itemName, e);
            return new ArrayList<>();
        }
    }

    /**
     * ★ 주문서 관리용: 품목 목록 API (별칭)
     */
    @GetMapping("/items")
    @ResponseBody
    public List<BsnItemDTO> items(@RequestParam(required = false) String name) {
        return itemList(name); // 위의 itemList 메서드 재사용
    }

    @GetMapping("/item/search")
    @ResponseBody
    public List<BsnItemDTO> itemSearch(@RequestParam String keyword){
        try {
            return keyword!=null && keyword.trim().length()>=2
                ? itemService.searchItemsByName(keyword.trim())
                : new ArrayList<>();
        } catch(Exception e){
            log.error("품목 검색 오류: {}", keyword, e); return new ArrayList<>();
        }
    }

    /*──────────────────────────────
     * 5. 디버깅 및 테스트용 API
     *──────────────────────────────*/
    
    /**
     * ★ 테스트용: 현재 사용자/회사 정보 확인
     */
    @GetMapping("/debug/auth-info")
    @ResponseBody
    public Map<String,Object> getAuthInfo(){
        Map<String,Object> info = new HashMap<>();
        try {
            info.put("coIdx", AuthUtil.getCoIdx());
            info.put("empIdx", AuthUtil.getEmpIdx());
            info.put("userIdx", AuthUtil.getUserIdx());
            info.put("mstIdx", AuthUtil.getMstIdx());
            info.put("success", true);
            
            log.info("인증 정보 조회: {}", info);
        } catch (Exception e) {
            log.error("인증 정보 조회 실패", e);
            info.put("success", false);
            info.put("error", e.getMessage());
        }
        return info;
    }

    /**
     * ★ 테스트용: 견적서 원시 데이터 확인
     */
    @GetMapping("/debug/quotation-raw")
    @ResponseBody
    public Map<String,Object> getQuotationRawData(){
        Map<String,Object> result = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("견적서 원시 데이터 조회 시작 - 회사: {}", coIdx);
            
            List<QuotationDTO> quotations = quotationService.getQuotationList();
            
            result.put("coIdx", coIdx);
            result.put("quotationCount", quotations.size());
            result.put("quotations", quotations);
            result.put("success", true);
            
            if (!quotations.isEmpty()) {
                result.put("firstQuotation", quotations.get(0));
            }
            
            log.info("견적서 원시 데이터 조회 완료 - 건수: {}", quotations.size());
            
        } catch (Exception e) {
            log.error("견적서 원시 데이터 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("coIdx", coIdx);
        }
        
        return result;
    }

    /**
     * ★ 테스트용: 특정 견적서 상세 원시 데이터 확인
     */
    @GetMapping("/debug/quotation-detail-raw")
    @ResponseBody
    public Map<String,Object> getQuotationDetailRawData(@RequestParam String quotationNo){
        Map<String,Object> result = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("견적서 상세 원시 데이터 조회 - 회사: {}, 견적서: {}", coIdx, quotationNo);
            
            List<QuotationDetailDTO> details = quotationService.getQuotationDetails(quotationNo);
            
            result.put("coIdx", coIdx);
            result.put("quotationNo", quotationNo);
            result.put("detailCount", details.size());
            result.put("details", details);
            result.put("success", true);
            
            log.info("견적서 상세 원시 데이터 조회 완료 - 건수: {}", details.size());
            
        } catch (Exception e) {
            log.error("견적서 상세 원시 데이터 조회 실패 - 회사: {}, 견적서: {}", coIdx, quotationNo, e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("coIdx", coIdx);
            result.put("quotationNo", quotationNo);
        }
        
        return result;
    }

    /*──────────────────────────────
     * 6. 출고 뷰
     *──────────────────────────────*/
    @GetMapping("/obound_list")
    public String outboundList(){ return "bsn/soutbound_list"; }

    @GetMapping("/soutbound")
    public String soutboundPage(Model model) {
        model.addAttribute("outbound", new SalesOutboundDTO());
        return "bsn/soutbound";
    }

    /*──────────────────────────────
     * 7. 출고 관리 영역
     *──────────────────────────────*/

    /**
     * ★ 출고 목록 조회 API
     */
    @GetMapping("/soutbounds")
    @ResponseBody
    public List<SalesOutboundDTO> soutboundsList() {
        try {
            return sOutboundService.getOutboundList();
        } catch (Exception e) {
            log.error("출고 목록 조회 오류", e);
            return new ArrayList<>();
        }
    }

    /**
     * ★ 출고 상세 조회 API
     */
    @GetMapping("/soutbounds/{outboundNo}/details")
    @ResponseBody
    public List<SoutboundDetailDTO> soutboundDetails(@PathVariable String outboundNo) {
        try {
            return sOutboundService.getOutboundDetails(outboundNo);
        } catch (Exception e) {
            log.error("출고 상세 조회 오류: {}", outboundNo, e);
            return new ArrayList<>();
        }
    }

    /**
     * ★ 주문서 기반 출고 생성 API (핵심 비즈니스 로직)
     */
    @PostMapping("/soutbounds/create-from-order")
    @ResponseBody
    public Map<String, Object> createOutboundFromOrder(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        
        try {
            String orderNo = request.get("orderNo");
            
            log.info("주문서 기반 출고 생성 요청 - 회사: {}, 사원: {}, 주문번호: {}", 
                    coIdx, empIdx, orderNo);
            
            // ✅ StringUtils 없이 검증
            if (orderNo == null || orderNo.trim().isEmpty()) {
                throw new IllegalArgumentException("주문서 번호가 필요합니다.");
            }
            
            // ★ 1. 주문서 존재 여부 확인
            OrdersDTO order = ordersService.getOrder(orderNo);
            if (order == null || order.getOrderNo() == null || order.getOrderNo().trim().isEmpty()) {
                throw new RuntimeException("주문서를 찾을 수 없습니다: " + orderNo);
            }
            
            // ★ 2. 주문서 기반 출고 생성
            String outboundNo = sOutboundService.createOutboundFromOrder(orderNo);
            
            // ★ 3. 주문서 상태 업데이트 (출고 처리)
            updateOrderStatusToShipped(orderNo);
            
            response.put("success", true);
            response.put("outboundNo", outboundNo);
            response.put("message", "주문서 기반 출고가 성공적으로 생성되었습니다.");
            
            log.info("주문서 기반 출고 생성 성공 - 주문번호: {} → 출고번호: {}", orderNo, outboundNo);
            
        } catch (IllegalArgumentException e) {
            log.warn("주문서 기반 출고 생성 실패 (유효성 검증) - 회사: {}, 오류: {}", coIdx, e.getMessage());
            response.put("success", false);
            response.put("message", "입력 오류: " + e.getMessage());
            
        } catch (RuntimeException e) {
            log.error("주문서 기반 출고 생성 실패 (비즈니스 로직) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "처리 오류: " + e.getMessage());
            
        } catch (Exception e) {
            log.error("주문서 기반 출고 생성 실패 (시스템 오류) - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시스템 오류가 발생했습니다. 관리자에게 문의하세요.");
        }
        
        return response;
    }

    /**
     * ★ 출고 등록 API
     */
    @PostMapping("/soutbounds")
    @ResponseBody
    public Map<String, Object> createOutbound(@RequestBody SalesOutboundDTO dto) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("출고 등록 요청 - 회사: {}, 거래처: {}", coIdx, dto.getCustomerCd());
            
            String outboundNo = sOutboundService.createOutbound(dto);
            
            response.put("success", true);
            response.put("outboundNo", outboundNo);
            response.put("message", "출고가 성공적으로 등록되었습니다.");
            
            log.info("출고 등록 성공 - 출고번호: {}", outboundNo);
            
        } catch (Exception e) {
            log.error("출고 등록 실패 - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "출고 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * ★ 출고 삭제 API
     */
    @DeleteMapping("/soutbounds/{outboundNo}")
    @ResponseBody
    public Map<String, Object> deleteOutbound(@PathVariable String outboundNo) {
        Map<String, Object> response = new HashMap<>();
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            log.info("출고 삭제 요청 - 회사: {}, 출고번호: {}", coIdx, outboundNo);
            
            sOutboundService.deleteOutbound(outboundNo);
            
            response.put("success", true);
            response.put("message", "출고가 성공적으로 삭제되었습니다.");
            
            log.info("출고 삭제 성공 - 출고번호: {}", outboundNo);
            
        } catch (Exception e) {
            log.error("출고 삭제 실패 - 회사: {}, 출고번호: {}, 오류: {}", coIdx, outboundNo, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "출고 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * ★ 주문서 상태를 '출고완료'로 업데이트
     */
    private void updateOrderStatusToShipped(String orderNo) {
        try {
            // 주문서 상세의 출고 상태를 '출고완료'로 업데이트
            List<OrdersDetailDTO> details = ordersService.getOrderDetails(orderNo);
            
            details.forEach(detail -> {
                detail.setOutState("출고완료");
                detail.setOutboundDt(java.time.LocalDate.now());
            });
            
            ordersService.updateOrderDetails(orderNo, details);
            
            log.info("주문서 상태 업데이트 완료 - 주문번호: {} → 출고완료", orderNo);
            
        } catch (Exception e) {
            log.error("주문서 상태 업데이트 실패 - 주문번호: {}, 오류: {}", orderNo, e.getMessage(), e);
            // ★ 주문서 상태 업데이트 실패는 출고 생성을 중단시키지 않음 (로그만 남김)
        }
    }

    /**
     * 📊 영업 대시보드 메인 화면
     */
    @GetMapping("/dashboard")
    public String salesDashboard(Model model) {
        String coIdx = AuthUtil.getCoIdx();
        log.info("영업 대시보드 접근 - 회사: {}", coIdx);

        try {
            // 변경: "bsn/SalesDashboard" 템플릿으로 매핑
            return "bsn/SalesDashboard";
        } catch (Exception e) {
            log.error("영업 대시보드 접근 실패 - 회사: {}", coIdx, e);
            return "redirect:/bsn";
        }
    }

    /**
     * 📈 고객별 매출 분석 화면
     */
    @GetMapping("/analytics/customer")
    public String customerAnalytics() {
        return "bsn/analytics/customer";
    }

    /**
     * 📦 상품별 수익성 분석 화면  
     */
    @GetMapping("/analytics/product")
    public String productAnalytics() {
        return "bsn/analytics/product";
    }

    /**
     * 👥 영업사원 성과 분석 화면
     */
    @GetMapping("/analytics/employee")
    public String employeeAnalytics() {
        return "bsn/analytics/employee";
    }

    /**
     * 📊 매출 트렌드 분석 화면
     */
    @GetMapping("/analytics/trend")
    public String trendAnalytics() {
        return "bsn/analytics/trend";
    }

    /**
     * 📋 일별 매출 리포트 화면
     */
    @GetMapping("/reports/daily")
    public String dailyReport() {
        return "bsn/reports/daily";
    }

    /**
     * 📅 월별 실적 리포트 화면
     */
    @GetMapping("/reports/monthly") 
    public String monthlyReport() {
        return "bsn/reports/monthly";
    }

    /**
     * 📆 연간 종합 리포트 화면
     */
    @GetMapping("/reports/annual")
    public String annualReport() {
        return "bsn/reports/annual";
    }

    /**
     * ⚙️ 맞춤형 리포트 화면
     */
    @GetMapping("/reports/custom")
    public String customReport() {
        return "bsn/reports/custom";
    }

    /**
     * 🔍 거래처 검색 결과 화면
     */
    @GetMapping("/customer_search")
    public String customerSearch(@RequestParam String keyword, Model model) {
        String coIdx = AuthUtil.getCoIdx();

        try {
            log.info("거래처 검색 - 회사: {}, 키워드: {}", coIdx, keyword);

            List<CustomerDTO> customers = customerService.searchCustomers(keyword);
            model.addAttribute("customers", customers);
            model.addAttribute("keyword", keyword);

            return "bsn/customer_search_results";
        } catch (Exception e) {
            log.error("거래처 검색 실패 - 회사: {}, 키워드: {}", coIdx, keyword, e);
            model.addAttribute("customers", new ArrayList<>());
            model.addAttribute("keyword", keyword);
            model.addAttribute("error", "검색 중 오류가 발생했습니다.");
            return "bsn/customer_search_results";
        }
    }

    /**
     * 📊 오늘 실적 리포트 (팝업용)
     */
    @GetMapping("/reports/today")
    public String todayReport(Model model) {
        String coIdx = AuthUtil.getCoIdx();

        try {
            model.addAttribute("reportDate", LocalDate.now());

            return "bsn/reports/today_popup";
        } catch (Exception e) {
            log.error("오늘 실적 리포트 조회 실패 - 회사: {}", coIdx, e);
            return "bsn/reports/today_popup";
        }
    }
}