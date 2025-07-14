package com.eflix.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.eflix.common.jasperResolver.JasperDownloadPDF;
import com.eflix.common.jasperResolver.JasperPreviewPDF;
import com.eflix.common.payment.service.BillingService;
import com.eflix.common.payment.service.PaymentService;
import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.dto.etc.StatementDTO;
import com.eflix.main.dto.etc.SubscriptionProcedureDTO;
import com.eflix.main.service.ReportService;
import com.eflix.main.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionRestController {
    
    @Value("${upload.path}")
    private String path;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private JasperPreviewPDF jasperPreviewPDF;

    @Autowired
    private JasperDownloadPDF jasperDownloadPDF;

    private final String STATEMENT_REPORT_PATH = "/reports/statement_template.jasper";
    private final String INVOICE_REPORT_PATH = "/reports/invoice_template.jasper";


    @PostMapping("/contract")
    public ResponseEntity<ResResult> postMethodName(@RequestBody Map<String,String> data) {
        log.info(data.toString());
        ResResult result;

        String contractHTML = data.get("contractHTML");
        String contractName = data.get("contractName");

        // 경로 생성
        File directory = new File(path + "/contract");
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 파일 경로 설정 (파일명에 .html 확장자 포함)
        File htmlFile = new File(directory, contractName + ".html");

        try (FileWriter writer = new FileWriter(htmlFile)) {
            writer.write(contractHTML);

            log.info("Saved HTML to: " + htmlFile.getAbsolutePath());

            result = ResUtil.makeResult(ResStatus.OK, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = ResUtil.makeResult("400", "파일을 저장하던 중 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResResult> postMethodName(@RequestBody SubscriptionProcedureDTO dto) {
        ResResult result = null;
        System.out.println(dto.toString());
        try {
            subscriptionService.insertSubscriptionByProcedure(dto);
            // subscriptionService.insertSubscriptionInfo(subMasterDTO.getSubscriptionDTO(), subMasterDTO.getMasterDTO());
            result = ResUtil.makeResult(ResStatus.OK, null);
        } catch (Exception e) {
            result = ResUtil.makeResult("400", e.getMessage(), null);
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/report/statement/{spiIdx}")
    public ModelAndView generateStatement(@PathVariable String spiIdx) {
        ModelAndView mv = new ModelAndView();

        StatementDTO statementDTO = subscriptionService.findSubscriptionBySpiIdx(spiIdx);
        List<StatementDTO> dataList = List.of(statementDTO);

        Map<String, Object> params = new HashMap<>();
        params.put("title", "구독 명세서");

        mv.setView(jasperDownloadPDF);
        mv.addObject("fileName", STATEMENT_REPORT_PATH);
        mv.addObject("dataList", dataList);
        mv.addObject("params", params);
        mv.addObject("saveName", "subscription_statement_" + statementDTO.getSpiUid());

        return mv;
    }

    @GetMapping("/report/preview/statement/{spiIdx}")
    public ModelAndView previewStatement(@PathVariable String spiIdx) throws Exception {
        ModelAndView mv = new ModelAndView();

        StatementDTO statementDTO = subscriptionService.findSubscriptionBySpiIdx(spiIdx);
        List<StatementDTO> dataList = List.of(statementDTO);
        log.info("dataList = {}", statementDTO);
        log.info("dataList = {}", statementDTO.getSpiStatus());

        Map<String, Object> params = new HashMap<>();
        params.put("title", "구독 명세서");

        mv.setView(jasperPreviewPDF);
        mv.addObject("fileName", STATEMENT_REPORT_PATH);
        mv.addObject("dataList", dataList);
        mv.addObject("params", params);
        mv.addObject("saveName", "subscription_statement_" + statementDTO.getSpiUid());

        return mv;
    }
    
    @GetMapping("/report/invoice/{spiIdx}")
    public ModelAndView generateInvoice(@PathVariable String spiIdx) {
        ModelAndView mv = new ModelAndView();

        InvoiceDTO invoiceDTO = subscriptionService.findSubscriptionInvoiceBySpiIdx(spiIdx);
        List<InvoiceDTO> dataList = List.of(invoiceDTO);

        Map<String, Object> params = new HashMap<>();
        params.put("title", "세금 계산서");

        mv.setView(jasperDownloadPDF);
        mv.addObject("fileName", INVOICE_REPORT_PATH);
        mv.addObject("dataList", dataList);
        mv.addObject("params", params);
        mv.addObject("saveName", "subscription_invoice_" + invoiceDTO.getSpiUid());

        return mv;
    }

    @GetMapping("/report/preview/invoice/{spiIdx}")
    public ModelAndView previewInvoice(@PathVariable String spiIdx) {
        ModelAndView mv = new ModelAndView();

        InvoiceDTO invoiceDTO = subscriptionService.findSubscriptionInvoiceBySpiIdx(spiIdx);
        List<InvoiceDTO> dataList = List.of(invoiceDTO);

        Map<String, Object> params = new HashMap<>();
        params.put("title", "세금 계산서");

        mv.setView(jasperPreviewPDF);
        mv.addObject("fileName", INVOICE_REPORT_PATH);
        mv.addObject("dataList", dataList);
        mv.addObject("params", params);
        mv.addObject("saveName", "subscription_invoice_" + invoiceDTO.getSpiUid());

        return mv;
    }
    

    @PostMapping("/statment/bulk")
    public ResponseEntity<byte[]> generateBulkInvoices(@RequestBody List<String> spiIdxList) {
        try {
            byte[] zipBytes = reportService.generateBulkInvoicesZip(spiIdxList);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("subscription_invoices.zip", StandardCharsets.UTF_8)
                    .build());
            return ResponseEntity.ok().headers(headers).body(zipBytes);
        } catch (Exception e) {
            log.error("ZIP 생성 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/compile-report")
    public ResponseEntity<String> compileReport() {
        try {
            String resultPath = reportService.compileReport();
            return ResponseEntity.ok("컴파일 완료: " + resultPath);
        } catch (Exception e) {
            log.error("컴파일 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("컴파일 실패: " + e.getMessage());
        }
    }

}
