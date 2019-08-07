package com.springsthursday.tapxt.util;

import com.apollographql.apollo.ApolloClient;
import com.springsthursday.tapxt.repository.UserInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApolloClientObject {

    public static ApolloClient getApolloClient()
    {
        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl("https://tapxt-dev.herokuapp.com/")
                .okHttpClient(
                        new OkHttpClient.Builder()
                                .connectTimeout(5000, TimeUnit.SECONDS)
                                .writeTimeout(5000, TimeUnit.SECONDS)
                                .readTimeout(5000, TimeUnit.SECONDS)
                                .addInterceptor(new Interceptor() {
                                     @Override
                                     public Response intercept(Chain chain) throws IOException {
                                         if (UserInfo.getInstance().userInfoItem.getToken().isEmpty() == false) {
                                             final String bearer = "BEARER " + UserInfo.getInstance().userInfoItem.getToken();
                                             final Request.Builder builder = chain.request().newBuilder()
                                                     .header("Authorization", bearer);

                                             return chain.proceed(builder.build());
                                         } else {
                                             return chain.proceed(chain.request());
                                         }
                                     }
                                 })
                                .build()
                )
                .build();

        return apolloClient;
    }
}
