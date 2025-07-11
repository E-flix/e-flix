package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class VaSummaryDTO {
    private int total;
    private int used;
    private int remaining;
    private int pending;
    private int approved;
    private int rejected;
}
