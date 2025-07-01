package com.eflix.common.jasperResolver;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

// 0701 최초 생성

@Component
public class jasperPreviewPDF extends AbstractView {

    @Autowired
    DataSource dataSource;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        
        String reportFile = (String) model.get("report"); // 예: "/reports/statement.jasper"
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) model.get("param");

        InputStream jasperStream = getClass().getResourceAsStream(reportFile);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            response.setContentType(getContentType());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } finally {
            if (conn != null) conn.close();
        }
    }
}
