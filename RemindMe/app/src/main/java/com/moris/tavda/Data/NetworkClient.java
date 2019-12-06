package com.moris.tavda.Data;

import android.content.Context;

import com.moris.tavda.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class NetworkClient {
//    private static final String BASE_URL = "http://192.168.1.12:8070";
//    private static final String BASE_URL = "http://db4free.net:3360";
    private static final String BASE_URL = "https://oddball-stomachs.000webhostapp.com";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    // TODO: 11/23/2019  HttpUrlConnection
                    .addInterceptor(interceptor)
                    .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                    .readTimeout(5, TimeUnit.MINUTES) // read timeout
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface UploadAPIs {
        @Multipart
        @POST("/index.php")
        Call<ResponseBody> uploadImage(@Part("action") RequestBody action,
                                       @Part("text") RequestBody text,
                                       @Part MultipartBody.Part images
                                       );
    }
}
