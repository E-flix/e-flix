package com.eflix.common.res;

import com.eflix.common.res.result.ResHead;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;

public class ResUtil {
    public static ResResult makeResult(ResStatus status, Object data){
        return ResResult.builder()
                .head(ResHead.builder().res_code(status.getCode()).res_result(status.getMsg()).build())
                .body(data)
                .build();
    }

    public static ResResult makeResult(String resultCode, String resultMsg,  Object data){
        return ResResult.builder()
                .head(ResHead.builder().res_code(resultCode).res_result(resultMsg).build())
                .body(data)
                .build();
    }
}