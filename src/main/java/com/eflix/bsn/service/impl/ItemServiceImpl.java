// src/main/java/com/eflix/bsn/service/impl/ItemServiceImpl.java
package com.eflix.bsn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.mapper.ItemMapper;
import com.eflix.bsn.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public List<BsnItemDTO> getAllItems() {
        return itemMapper.selectAllItems();
    }

    @Override
    public BsnItemDTO getItemByCode(String itemCode) {
        return itemMapper.selectItemByCode(itemCode);
    }

    @Override
    public List<BsnItemDTO> searchItemsByName(String itemName) {
        return itemMapper.selectItemsByName(itemName);
    }
}
