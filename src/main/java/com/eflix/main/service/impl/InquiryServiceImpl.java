package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.AnswerDTO;
import com.eflix.main.dto.InquiryDTO;
import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.etc.InquirySearchDTO;
import com.eflix.main.mapper.InquiryMapper;
import com.eflix.main.service.InquiryService;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryMapper inquiryMapper;

    @Override
    public int insertQst(QuestionDTO questionDTO) {
        return inquiryMapper.insertQst(questionDTO);
    }

    @Override
    public int insertAns(AnswerDTO answerDTO) {
        return inquiryMapper.insertAns(answerDTO);
    }

    @Override
    public int findAllInquiryCount(InquirySearchDTO inquirySearchDTO) {
        return inquiryMapper.findAllInquiryCount(inquirySearchDTO);
    }

    @Override
    public List<InquiryDTO> findAllInquiry(InquirySearchDTO inquirySearchDTO) {
        return inquiryMapper.findAllInquiry(inquirySearchDTO);
    }

    @Override
    public QuestionDTO findQstByIdx(String qstIdx) {
        return inquiryMapper.findQstByIdx(qstIdx);
    }

    @Override
    public AnswerDTO findAnsByQstIdx(String qstIdx) {
        return inquiryMapper.findAnsByQstIdx(qstIdx);
    }

}
