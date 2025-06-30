package com.eflix.bsn.service;

import com.eflix.bsn.dto.CreditInfoDTO;

public interface CreditService {
  CreditInfoDTO getCreditByCustomer(String customerCd);
}
