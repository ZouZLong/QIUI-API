package com.example.openplatform.api.builder;


import java.io.File;

import com.example.openplatform.api.request.PostFileRequest;
import com.example.openplatform.api.request.RequestCall;
import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {
    private File file;
    private MediaType mediaType;


    public OkHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType, id).build();
    }


}
