package com.eflix.acc.service.impl;

import com.eflix.acc.mapper.EntryAutoMapper;
import com.eflix.acc.service.EntryAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class EntryAutoServiceImpl implements EntryAutoService {
  @Autowired
  private EntryAutoMapper entryAutoDetailMapper;

  @Override
  public List<Map<String, Object>> getDetailList() {
    return entryAutoDetailMapper.selectDetailList();
  }
}
