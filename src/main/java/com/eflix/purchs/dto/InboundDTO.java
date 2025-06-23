package com.eflix.purchs.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InboundDTO {
    String inboundId;
    String inboundLot;
    Date inboundDate;
    String memo;
    String inboundManager;
}
