package com.esf.quagnitia.messaging_app.webservice;


import com.esf.quagnitia.messaging_app.Model.PagingItem;
import com.esf.quagnitia.messaging_app.Model.Req;
import com.esf.quagnitia.messaging_app.Model.User;
import com.esf.quagnitia.messaging_app.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
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
    Call<UserResponse> getSchhollist(@Field("userId") String id,
                                     @Field("page") String page
            , @Header("sessionId") String sessionId
            , @Field("fcmTokenId") String fcmTokenId);

    @FormUrlEncoded
    @POST(ConstatntsApi.MessageList)
    Call<UserResponse> getSchoolMessage(@Field("userId") String id, @Field("page") String page, @Field("aqiSchoolID") String scid, @Field("fromDate") String fromDate,
                                        @Field("toDate") String toDate, @Header("sessionId") String sessionId, @Field("fcmTokenId") String fcmTokenId);

    @FormUrlEncoded
    @POST(ConstatntsApi.SchoolMap)
    Call<UserResponse> getSchoolMap(@Field("userId") String id, @Field("aqiSchoolID") String scid,@Header("sessionId") String sessionId);


    @POST(ConstatntsApi.SHOWMESSAGE2)
    Call<UserResponse> showNextMessage2(@Body PagingItem pagingItem);//@Query("next_page_url") String next_page_url);

}
