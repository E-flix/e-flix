package com.eflix.mgr.service;

import com.eflix.mgr.dto.etc.AccDTO;
import com.eflix.mgr.dto.etc.BsnDTO;
import com.eflix.mgr.dto.etc.HrDTO;
import com.eflix.mgr.dto.etc.PurchsDTO;
import com.eflix.mgr.dto.etc.SummaryDTO;

public interface SummaryService {

    public SummaryDTO getSummary();

    public HrDTO getHr();

    public AccDTO getAcc();

    public PurchsDTO getPurchs();

    public BsnDTO getBsn();
}
