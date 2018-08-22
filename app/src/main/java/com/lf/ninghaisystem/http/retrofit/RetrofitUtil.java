package com.lf.ninghaisystem.http.retrofit;

import  android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lf.ninghaisystem.BuildConfig;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RetrofitUtil {

    public static final String HOST = "https://lz.nbnhrd.gov.cn/ninghai-APP/";
    private static Retrofit retrofit;
    private static NhrdlzService apiService;
    private static boolean isInit = false;
    public static void init(Context context) throws IOException{
        if(!isInit){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger(){

                @Override
                public void log(String message) {
                    if(BuildConfig.DEBUG) {
                        Log.i("OKHttp", message);
                    }
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            httpClient.setReadTimeout(15, TimeUnit.SECONDS);
            httpClient.setWriteTimeout(15, TimeUnit.SECONDS);
            httpClient.setRetryOnConnectionFailure(true);
            if(BuildConfig.DEBUG) {
                httpClient.interceptors().add(logging);  // <-- this is the important line!
            }else {
                httpClient.interceptors().add(new MyInterceptor());
            }
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:s").create();
            retrofit = new Retrofit.Builder().baseUrl(HOST).addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient).build();
            isInit = true;
        }
    }

    public static NhrdlzService getService(){
        if(apiService == null){
            apiService =  retrofit.create(NhrdlzService.class);
        }
        return apiService;
    }

    public static class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
//            long t1 = System.nanoTime();
            Response response = chain.proceed(request);
//            long t2 = System.nanoTime();
//            Log.d(String.format("响应请求 %s %.1fms", response.request().url(), (t2 - t1) / 1e6d));
            return response;
        }
    }

}
