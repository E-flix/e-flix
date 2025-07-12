package com.eflix.common.jasperResolver;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

// 0701 최초 생성

@Component
public class JasperPreviewPDF extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        String reportFile = (String)model.get("fileName");

        Map<String,Object> map = (Map<String,Object>) model.get("params");

        List<Object> dataList = (List<Object>) model.get("dataList");

        InputStream jasperStream = getClass().getResourceAsStream(reportFile);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        JasperPrint jasperPrint =  JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(dataList));

        response.setContentType("application/pdf");
        // response.setHeader("Content-Disposition", "attachment; filename=" + model.get("saveName"));
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
}
