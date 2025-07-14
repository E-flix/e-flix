package com.eflix.common.chat.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String roomIdx;
    private String senderIdx;
    private String senderName;
    private String message;
    private Date sendTime;
    private String readYn;
}