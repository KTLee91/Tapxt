package com.springsthursday.tapxt.util;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
public interface UploadImageInterface {

        /*@Multipart
        @POST("/api/upload")
        Call<ResponseBody> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);*/

        @Multipart
        @POST("api/upload")
        Call<UploadObject> uploadFile(@Part MultipartBody.Part file);
}