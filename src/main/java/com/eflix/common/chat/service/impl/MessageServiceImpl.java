package com.eflix.common.chat.service.impl;

import org.springframework.stereotype.Service;

import com.eflix.common.chat.dto.ChatMessageDTO;
import com.eflix.common.chat.dto.ChatReadDTO;
import com.eflix.common.chat.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{

    @Override
    public void insertMessage(ChatMessageDTO chatMessageDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertMessage'");
    }

    @Override
    public void markAsRead(ChatReadDTO chatReadDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAsRead'");
    }
    
}
