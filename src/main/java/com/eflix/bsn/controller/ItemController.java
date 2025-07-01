// src/main/java/com/eflix/bsn/controller/ItemController.java
package com.eflix.bsn.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.service.ItemService;

@RestController
@RequestMapping("/bsn/item")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /** 전체 품목 조회 */
    @GetMapping("/list")
    public List<BsnItemDTO> list(@RequestParam(value = "itemName", required = false) String itemName) {
        if (itemName != null && !itemName.isBlank()) {
            return itemService.searchItemsByName(itemName);
        }
        return itemService.getAllItems();
    }

    /** 단건 조회 (필요 시) */
    @GetMapping("/{code}")
    public BsnItemDTO getOne(@PathVariable("code") String code) {
        return itemService.getItemByCode(code);
    }
}
