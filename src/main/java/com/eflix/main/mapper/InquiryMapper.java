package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.AnswerDTO;
import com.eflix.main.dto.InquiryDTO;
import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.etc.InquirySearchDTO;

public interface InquiryMapper {

    public int insertQst(QuestionDTO questionDTO);

    public int insertAns(AnswerDTO answerDTO);

    public int findAllInquiryCount(InquirySearchDTO inquirySearchDTO);

    public List<InquiryDTO> findAllInquiry(InquirySearchDTO inquirySearchDTO);

    public QuestionDTO findQstByIdx(String qstIdx);

    public AnswerDTO findAnsByQstIdx(String qstIdx);
}
