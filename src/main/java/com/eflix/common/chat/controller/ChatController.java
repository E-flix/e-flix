package com.eflix.common.chat.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.chat.dto.ChatMessageDTO;
import com.eflix.common.chat.service.MessageService;

@RestController
public class ChatController {

    @Autowired
    private MessageService messageService;
    
    // 0712
    public void send(ChatMessageDTO msg) {
        msg.setSendTime(new Date());
        messageService.insertMessage(msg);
    }
}
