package com.eflix.hr.dto.etc;

import java.util.List;

import lombok.Data;

@Data
public class BulkDTO {
    List<String> leaveReqIdxs;
    private String type;
    private String abReason;
}
