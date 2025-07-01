package com.eflix.main.service;

import java.util.List;
import java.util.Map;

import com.eflix.main.dto.CompanyDTO;

// 최초 생성 0630
public interface ReportService {
    public byte[] generateContractReport(String spiIdx) throws Exception;
    
    public byte[] generateStatementReport(String spiIdx) throws Exception;

    public byte[] generateInvoiceReport(String spiIdx) throws Exception;

    public byte[] generateInvoicePdf(String spiIdx) throws Exception;

    public byte[] generateBulkInvoicesZip(List<String> spiIdxList) throws Exception;

    public String compileReport() throws Exception;

    public byte[] generateStatmentPdf(String spiIdx) throws Exception;

    public Map<String, Object> buildParameters(CompanyDTO companyDTO);
}
