package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.AnswerDTO;
import com.eflix.main.dto.InquiryDTO;
import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.etc.InquirySearchDTO;

public interface InquiryMapper {

    int insertQst(QuestionDTO questionDTO);

    int insertAns(AnswerDTO answerDTO);

    int findAllInquiryCount(InquirySearchDTO inquirySearchDTO);

    List<InquiryDTO> findAllInquiry(InquirySearchDTO inquirySearchDTO);

    QuestionDTO findQstByIdx(String qstIdx);

    AnswerDTO findAnsByQstIdx(String qstIdx);
    
}
