package com.eflix.acc.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface EntryAutoMapper {
  List<Map<String, Object>> selectDetailList();
}
