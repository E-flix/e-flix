package com.eflix.bsn.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.mapper.QuotationMapper;
import com.eflix.bsn.service.QuotationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationMapper quotationMapper;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public List<QuotationDTO> getQuotationList() {
        return quotationMapper.getQuotationList();
    }

    @Override
    public String generateNextQuotationNo() {
        String today = LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String lastNo = quotationMapper.findLastQuotationNo();  // ex) "QT-20250610-005"
        int nextSeq = 1;

        if (lastNo != null && lastNo.startsWith("QT-")) {
            try {
                String[] parts = lastNo.split("-");
                nextSeq = Integer.parseInt(parts[2]) + 1;
            } catch (Exception e) {
                nextSeq = 1;
            }
        }

        String seqStr = String.format("%03d", nextSeq);
        return "QT-" + today + "-" + seqStr;
    }

    @Override
    @Transactional
    public void createQuotation(QuotationDTO dto) {
        // 1) PK 생성
        dto.setQuotationNo(generateNextQuotationNo());
        // 2) 메인 삽입
        quotationMapper.insertQuotation(dto);
        // 3) 디테일 삽입
        List<QuotationDetailDTO> details = dto.getDetails();
        for (int i = 0; i < details.size(); i++) {
            QuotationDetailDTO d = details.get(i);
            d.setQuotationNo(dto.getQuotationNo());
            d.setLineNo(i + 1);
            quotationMapper.insertQuotationDetail(d);
        }
    }
}
