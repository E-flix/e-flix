package com.eflix.acc.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eflix.acc.dto.PartnerDetailDTO;
import com.eflix.acc.mapper.PartnerMapper;
import com.eflix.acc.service.PartnerService;
import lombok.RequiredArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-23
  - 설명     : partnerServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-23 (김희정): 일반거래처 조회 작성
=============================================== */
@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

  private final PartnerMapper partnerMapper;

  // 일반거래처 전체조회
  @Override
  public List<PartnerDetailDTO> getList() {
    return partnerMapper.getList();
  }

  // 거래처 코드로 일반거래처 조회
  @Override
  public PartnerDetailDTO getListByCode(int partnerCode) {
    return partnerMapper.getListByCode(partnerCode);
  }

  // 거래처 이름으로 일반거래처 조회 (부분검색)
  @Override
  public List<PartnerDetailDTO> getListByName(String partnerName) {
    return partnerMapper.getListByName(partnerName);
  }

}
