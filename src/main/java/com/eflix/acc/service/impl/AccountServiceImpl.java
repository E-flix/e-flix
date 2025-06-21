package com.eflix.acc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import com.eflix.acc.dto.AccountDTO;
import com.eflix.acc.mapper.AccountMapper;
import com.eflix.acc.service.AccountService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : accountServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): 계정과목 전체조회 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountMapper accountMapper;

  /**
   * 계정과목 전체조회
   * 
   * @return List<AccountDTO>
   */
  @Override
  public List<AccountDTO> getList() {
    return accountMapper.getList();
  }

  // @Override
  // public List<BoardVO> findAll(BoardVO boardVO) {
  // return null;
  // }

  // @Override
  // public BoardVO findById(int bno) {
  // BoardVO result = boardMapper.findById(bno);
  // return result;
  // }

  // @Override
  // public int insert(BoardVO boardVO) {
  // int result = boardMapper.insert(boardVO);
  // return result;
  // }

  // @Override
  // public int update(BoardVO boardVO) {
  // return boardMapper.update(boardVO);
  // }

  // @Override
  // public int delete(int bno) {
  // return boardMapper.delete(bno);
  // }

  // @Override
  // public Long getTotal(Criteria cri) {
  // return boardMapper.getTotal(cri);
  // }
}
