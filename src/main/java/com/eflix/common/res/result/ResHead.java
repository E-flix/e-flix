package com.eflix.common.res.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResHead {
    private String res_code;
    private String res_result;
}
