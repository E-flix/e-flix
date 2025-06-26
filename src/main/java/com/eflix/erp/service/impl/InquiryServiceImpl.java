package com.eflix.erp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.erp.dto.AnswerDTO;
import com.eflix.erp.dto.QuestionDTO;
import com.eflix.erp.mapper.InquiryMapper;
import com.eflix.erp.service.InquiryService;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryMapper inquiryMapper;

    @Override
    public int insertQst(QuestionDTO questionDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertQuestion'");
        return inquiryMapper.insertQst(questionDTO);
    }

    @Override
    public int insertAns(AnswerDTO answerDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertAns'");

        return inquiryMapper.insertAns(answerDTO);
    }

}
