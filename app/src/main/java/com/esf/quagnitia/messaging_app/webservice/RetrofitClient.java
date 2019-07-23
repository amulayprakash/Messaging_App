package com.esf.quagnitia.messaging_app.webservice;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    public static Retrofit retrofit;
    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstatntsApi.BASE_URL)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


}
