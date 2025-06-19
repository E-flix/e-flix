package com.eflix.acc.service;

import java.util.List;
import com.eflix.acc.dto.AccountDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : accountService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): 계정과목 전체조회 작성
=============================================== */
public interface AccountService {
	// 계정과목 전체조회
  public List<AccountDTO> getList();
	
	// public Long getTotal(Criteria cri);

	// public List<BoardVO> findAll(BoardVO boardVO);

	// public BoardVO findById(int bno);

	// public int insert(BoardVO boardVO);

	// public int update(BoardVO boardVO);

	// public int delete(int bno);	
}
