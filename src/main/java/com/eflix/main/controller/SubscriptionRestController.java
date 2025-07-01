package com.eflix.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.main.dto.etc.SubMasterDTO;
import com.eflix.main.service.ReportService;
import com.eflix.main.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionRestController {
    
    @Value("${file.path}")
    private String path;

    @Autowired
    private SubscriptionService suubscriptionService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/contract")
    public ResponseEntity<ResResult> postMethodName(@RequestBody Map<String,String> data) {
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

    @PostMapping("/subscription")
    public ResponseEntity<ResResult> postMethodName(@RequestBody SubMasterDTO subMasterDTO) {
        ResResult result;
        try {
            suubscriptionService.insertSubscriptionInfo(subMasterDTO.getSubscriptionDTO(), subMasterDTO.getMasterDTO());
            result = ResUtil.makeResult(ResStatus.OK, null);
        } catch (Exception e) {
            result = ResUtil.makeResult("400", e.getMessage(), null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/report/statement/{spiIdx}")
    public ResponseEntity<byte[]> generateStatement(@PathVariable String spiIdx) {
        try {
            byte[] pdf = reportService.generateStatmentPdf(spiIdx);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("subscription_statement_" + spiIdx + ".pdf", StandardCharsets.UTF_8)
                    .build());
            headers.setContentLength(pdf.length);
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            log.error("PDF 생성 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/report/statement/preview/{spiIdx}")
    public ResponseEntity<byte[]> previewStatement(@PathVariable String spiIdx) {
        try {
            byte[] pdf = reportService.generateStatmentPdf(spiIdx);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("subscription_statement_preview_" + spiIdx + ".pdf", StandardCharsets.UTF_8)
                    .build());
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            log.error("PDF 미리보기 생성 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/report/invoice/{spiIdx}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable String spiIdx) {
        try {
            byte[] pdf = reportService.generateInvoicePdf(spiIdx);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("subscription_invoice_" + spiIdx + ".pdf", StandardCharsets.UTF_8)
                    .build());
            headers.setContentLength(pdf.length);
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            log.error("PDF 생성 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/report/invoice/preview/{spiIdx}")
    public ResponseEntity<byte[]> previewInvoice(@PathVariable String spiIdx) {
        try {
            byte[] pdf = reportService.generateInvoicePdf(spiIdx);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("subscription_invoice_preview_" + spiIdx + ".pdf", StandardCharsets.UTF_8)
                    .build());
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            log.error("PDF 미리보기 생성 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
