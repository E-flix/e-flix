// src/main/java/com/eflix/bsn/controller/ItemController.java
package com.eflix.bsn.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.eflix.bsn.dto.BsnItemDTO;
import com.eflix.bsn.service.ItemService;
import com.eflix.common.payment.dto.ItemDTO;

@RestController
@RequestMapping("/bsn/items")
public class ItemController {

    private final ItemService service;
    public ItemController(ItemService service) {
        this.service = service;
    }

    /** 전체 품목 조회 */
    @GetMapping("/list")
    public List<BsnItemDTO> list(@RequestParam(value = "itemName", required = false) String itemName) {
        if (itemName != null && !itemName.isBlank()) {
            return service.searchItemsByName(itemName);
        }
        return service.getAllItems();
    }

    /** 단건 조회 (필요 시) */
    @GetMapping("/{code}")
    public BsnItemDTO getOne(@PathVariable("code") String code) {
        return service.getItemByCode(code);
    }

    /** 품목 조회
     * GET /bsn/items
     * GET /bsn/items?name=검색어
     */
    @GetMapping
    public List<ItemDTO> searchItems(
        @RequestParam(value="name", required=false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return service.findByName(name);
        }
        return service.findAll();
    }
}
