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

import com.eflix.main.dto.CompanyDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.dto.etc.StatementDTO;
import com.eflix.main.mapper.CompanyMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.mapper.UserMapper;
import com.eflix.main.service.ReportService;

import lombok.extern.slf4j.Slf4j;
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
    private CompanyMapper companyMapper;
    
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    private final String STATEMENT_REPORT_PATH = "static/reports/statement_template.jasper";
    private final String INVOICE_REPORT_PATH = "static/reports/invoice_template.jasper";

    @Override
    public byte[] generateContractReport(String spiIdx) throws Exception {
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
    public byte[] generateInvoiceReport(String spiIdx) throws Exception {
        SubscriptionDTO dto = subscriptionMapper.findSubscriptionDetail(spiIdx);
        List<SubscriptionDTO> dataList = List.of(dto);

        InputStream reportStream = getClass().getResourceAsStream("/static/reports/invoice_template.jasper");
        if (reportStream == null) {
            throw new FileNotFoundException("Jasper 파일을 찾을 수 없습니다: /reports/invoice_template.jasper");
        }
        JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

        Map<String, Object> params = Map.of("title", "세금 계산서");

        JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(dataList));
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] generateStatementReport(String spiIdx) throws Exception {
        StatementDTO statementDTO = subscriptionMapper.findSubscriptionBySpiIdx(spiIdx);
        List<StatementDTO> dataList = List.of(statementDTO);

        InputStream reportStream = getClass().getResourceAsStream("/static/reports/statement_template.jasper");
        if (reportStream == null) {
            throw new FileNotFoundException("Jasper 파일을 찾을 수 없습니다: /reports/statement_template.jasper");
        }
        JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

        Map<String, Object> params = Map.of("title", "구독 명세서");

        JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(dataList));
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] generateStatmentPdf(String spiIdx) throws Exception {
        StatementDTO statementDTO = subscriptionMapper.findSubscriptionBySpiIdx(spiIdx);
        if (statementDTO == null) throw new RuntimeException("구독 정보를 찾을 수 없습니다: " + spiIdx);

        CompanyDTO companyDTO = companyMapper.findByCoIdx(statementDTO.getCoIdx());

        Map<String, Object> parameters = buildParameters(companyDTO);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(statementDTO));

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

    @Override
    public Map<String, Object> buildParameters(CompanyDTO companyDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("COMPANY_NAME", "E-FLIX");
        params.put("COMPANY_ADDRESS", "대구광역시 중구 중앙대로 403 (남일동 135-1, 5층)");
        params.put("COMPANY_PHONE", "053-421-2460");

        params.put("CUSTOMER_NAME", "담당자명");
        params.put("CUSTOMER_PHONE", "053-421-2460");
        params.put("CUSTOMER_EMAIL", "ask@yedam.ac");

        params.put("EMP_NAME", "팀장");

        return params;
    }

}