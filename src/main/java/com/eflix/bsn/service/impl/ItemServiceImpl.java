// src/main/java/com/eflix/bsn/service/impl/ItemServiceImpl.java
package com.eflix.bsn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.mapper.ItemMapper;
import com.eflix.bsn.service.ItemService;
import com.eflix.common.payment.dto.ItemDTO;

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

    @Override
    public List<ItemDTO> findAll() {
        return itemMapper.selectAll();
    }

    @Override
    public List<ItemDTO> findByName(String name) {
        return itemMapper.selectByName(name);
    }
}
