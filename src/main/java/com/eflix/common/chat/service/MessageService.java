package com.eflix.common.chat.service;

import com.eflix.common.chat.dto.ChatMessageDTO;
import com.eflix.common.chat.dto.ChatReadDTO;

public interface MessageService {
    public void insertMessage(ChatMessageDTO chatMessageDTO);
    public void markAsRead(ChatReadDTO chatReadDTO);
}
