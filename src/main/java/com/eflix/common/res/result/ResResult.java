package com.eflix.common.res.result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ResResult {
    private ResHead head;
    private Object body;
}