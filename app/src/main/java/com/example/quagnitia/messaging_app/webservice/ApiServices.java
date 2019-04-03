package com.example.quagnitia.messaging_app.webservice;


import com.example.quagnitia.messaging_app.Model.PagingItem;
import com.example.quagnitia.messaging_app.Model.Req;
import com.example.quagnitia.messaging_app.Model.User;
import com.example.quagnitia.messaging_app.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {
    @Headers({
            "Accept:application/json",
            "Content-Type: application/json"
    })

    @POST(ConstatntsApi.LOGIN_URL)
    Call<UserResponse> loginUser(@Body User user);

    @POST(ConstatntsApi.FORGOT_URL)
    Call<UserResponse> forgotPassword(@Query("email") String email);

   /* @POST(ConstatntsApi.SHOWMESSAGE)
    Call<MsgResponse> showMessage(@Body Req user);*/

    @POST(ConstatntsApi.SHOWMESSAGE2)
    Call<UserResponse> showMessage2(@Body Req user);

    @FormUrlEncoded
    @POST(ConstatntsApi.SchoolList)
    Call<UserResponse> getSchhollist(@Field("userId") String id, @Field("page") String page);

    @FormUrlEncoded
    @POST(ConstatntsApi.MessageList)
    Call<UserResponse> getSchoolMessage(@Field("aqiSchoolID") String id, @Field("fromDate") String fromDate,
                                        @Field("toDate") String toDate);

    @POST(ConstatntsApi.SHOWMESSAGE2)
    Call<UserResponse> showNextMessage2(@Body PagingItem pagingItem);//@Query("next_page_url") String next_page_url);

}
