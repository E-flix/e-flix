package com.eflix.main.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.CustomerDTO;
import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.mapper.UserMapper;
import com.eflix.main.service.CompanyService;
import com.eflix.main.service.ReportService;
import com.eflix.main.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

// 최초 생성 0630

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CompanyMapper companyMapper;
    
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Value("${file.path}")
    private String path;

    private final String STATEMENT_REPORT_PATH = "static/reports/statement_template.jasper";
    private final String INVOICE_REPORT_PATH = "static/reports/invoice_template.jasper";

    @Override
    public byte[] generateContractReport(Long spiIdx) throws Exception {
        SubscriptionDTO dto = subscriptionMapper.findSubscriptionDetail(spiIdx);
        List<SubscriptionDTO> dataList = List.of(dto);

        InputStream reportStream = getClass().getResourceAsStream("/static/reports/contract_template.jasper");
        if (reportStream == null) {
            throw new FileNotFoundException("Jasper 파일을 찾을 수 없습니다: /reports/contract_template.jasper");
        }
        JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

        Map<String, Object> params = Map.of("title", "구독 계약서");

        JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(dataList));
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] generateInvoiceReport(Long spiIdx) throws Exception {
        SubscriptionDTO dto = subscriptionMapper.findSubscriptionDetail(spiIdx);
        List<SubscriptionDTO> dataList = List.of(dto);

        InputStream reportStream = getClass().getResourceAsStream("/static/reports/invoice_template.jasper");
        if (reportStream == null) {
            throw new FileNotFoundException("Jasper 파일을 찾을 수 없습니다: /reports/invoice_template.jasper");
        }
        JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

        Map<String, Object> params = Map.of("title", "구독 명세서");

        JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(dataList));
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] generateStatmentPdf(String spiIdx) throws Exception {
        SubscriptionDTO subscriptionDTO = subscriptionMapper.findSubscriptionBySpiIdx(spiIdx);
        if (subscriptionDTO == null) throw new RuntimeException("구독 정보를 찾을 수 없습니다: " + spiIdx);

        CompanyDTO companyDTO = companyMapper.findByCoIdx(subscriptionDTO.getCoIdx());

        Map<String, Object> parameters = buildParameters(companyDTO);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(subscriptionDTO));

        try (InputStream jasperStream = new ClassPathResource(STATEMENT_REPORT_PATH).getInputStream()) {
            return JasperRunManager.runReportToPdf(jasperStream, parameters, dataSource);
        }
    }

    @Override
    public byte[] generateInvoicePdf(String spiIdx) throws Exception {
        InvoiceDTO invoiceDTO = subscriptionMapper.findSubscriptionInvoiceBySpiIdx(spiIdx);
        if (invoiceDTO == null) throw new RuntimeException("구독 정보를 찾을 수 없습니다: " + spiIdx);

        CompanyDTO companyDTO = companyMapper.findByCoIdx(invoiceDTO.getCoIdx());

        Map<String, Object> parameters = buildParameters(companyDTO);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(invoiceDTO));

        try (InputStream jasperStream = new ClassPathResource(INVOICE_REPORT_PATH).getInputStream()) {
            return JasperRunManager.runReportToPdf(jasperStream, parameters, dataSource);
        }
    }

    @Override
    public byte[] generateBulkInvoicesZip(List<String> spiIdxList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String spiIdx : spiIdxList) {
                try {
                    byte[] pdfBytes = generateInvoicePdf(spiIdx);
                    ZipEntry entry = new ZipEntry("subscription_invoice_" + spiIdx + ".pdf");
                    zos.putNextEntry(entry);
                    zos.write(pdfBytes);
                    zos.closeEntry();
                } catch (Exception e) {
                    log.error("PDF 생성 실패: {}", spiIdx, e);
                }
            }
        }
        return baos.toByteArray();
    }

    @Override
    public String compileReport() throws Exception {
        ClassPathResource jrxml = new ClassPathResource("/static/reports/subscription_invoice.jrxml");
        try (InputStream jrxmlStream = jrxml.getInputStream()) {
            JasperReport compiled = JasperCompileManager.compileReport(jrxmlStream);
            String outputPath = "src/main/resources/reports/subscription_invoice.jasper";
            JRSaver.saveObject(compiled, outputPath);
            return outputPath;
        }
    }

    private Map<String, Object> buildParameters(CompanyDTO companyDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("COMPANY_NAME", "E-FLIX");
        params.put("COMPANY_ADDRESS", "서울시 강남구 테헤란로 123");
        params.put("COMPANY_PHONE", "02-1234-5678");

        params.put("CUSTOMER_NAME", companyDTO != null ? companyDTO.getPschName() : "담당자명");
        params.put("CUSTOMER_PHONE", companyDTO != null ? companyDTO.getPschTel() : "053-421-2460");
        params.put("CUSTOMER_EMAIL", companyDTO != null ? companyDTO.getPschEmail() : "ask@yedam.ac");

        params.put("EMP_NAME", companyDTO != null ? companyDTO.getCeoName() : "대표자명");

        return params;
    }

}