// src/main/java/com/eflix/bsn/controller/ItemController.java
package com.eflix.bsn.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.service.ItemService;

@RestController
@RequestMapping("/bsn/item")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public List<BsnItemDTO> list(@RequestParam(value="itemName", required=false) String itemName) {
        List<BsnItemDTO> items = (itemName != null && !itemName.isBlank())
            ? itemService.searchItemsByName(itemName)
            : itemService.getAllItems();
        log.info("BSN_ITEM 조회 결과 건수 = {}", items.size());
        return items;
    }
}
