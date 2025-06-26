package com.eflix.main.mapper;

import com.eflix.main.dto.AnswerDTO;
import com.eflix.main.dto.QuestionDTO;

public interface InquiryMapper {

    int insertQst(QuestionDTO questionDTO);

    int insertAns(AnswerDTO answerDTO);
    
}
