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
    public String generateNextQuotationNo() {
        String today = LocalDate.now().format(fmt);
        String lastNo = quotationMapper.findLastQuotationNo();  // ex) "QT-20250610-005"
        int nextSeq = 1;

        if (lastNo != null && lastNo.startsWith("QT-")) {
            try {
                String[] parts = lastNo.split("-");
                // 날짜가 오늘과 같을 때만 시퀀스 증가
                if (parts.length == 3 && parts[1].equals(today)) {
                    nextSeq = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                nextSeq = 1;
            }
        }

        return String.format("QT-%s-%03d", today, nextSeq);
    }

    @Override
    @Transactional
    public void createQuotation(QuotationDTO dto) {
        // 1) PK 생성 및 설정
        String newNo = generateNextQuotationNo();
        dto.setQuotationNo(newNo);

        // 2) 메인 테이블에 삽입
        quotationMapper.insertQuotation(dto);

        // 3) 디테일 테이블에 삽입
        List<QuotationDetailDTO> details = dto.getDetails();
        if (details != null) {
            for (int i = 0; i < details.size(); i++) {
                QuotationDetailDTO d = details.get(i);
                d.setQuotationNo(newNo);
                d.setLineNo(i + 1);
                quotationMapper.insertQuotationDetail(d);
            }
        }
    }

    @Override
    public List<QuotationDTO> getQuotationList() {
        return quotationMapper.getQuotationList();
    }
}
