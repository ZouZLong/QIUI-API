package com.example.openplatform.api.builder;


import com.example.openplatform.api.OkHttpUtils;
import com.example.openplatform.api.request.OtherRequest;
import com.example.openplatform.api.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers, id).build();
    }
}
