package com.eflix.erp.mapper;

import com.eflix.erp.dto.AnswerDTO;
import com.eflix.erp.dto.QuestionDTO;

public interface InquiryMapper {

    int insertQst(QuestionDTO questionDTO);

    int insertAns(AnswerDTO answerDTO);
    
}
