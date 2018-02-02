package com.example.vicente.registerPhoneCall;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by vicente on 24/01/18.
 */

public interface RestClient {

    //  Obtendra las llamadas entrantes del servidor
    @GET("incomingCall")
    Call<ArrayList<PhoneCall>> getIncomingPhoneCalls();

    //  Subira la llamada entrante al servidor
    @POST("incomingCall")
    Call<PhoneCall> postIncomingPhoneCall(@Body PhoneCall llamada);

    //  Actualizara la llamada entrante del servidor
    @PUT("/incomingCall/{id}")
    @FormUrlEncoded
    Call<PhoneCall> updateIncomingPhoneCall(@Path("id") long id,
                                    @Field("duration") double duration,
                                    @Field("numberPhone") String numberPhone,
                                    @Field("state") String state,
                                    @Field("time") String time);

    //  Obtendra las llamadas salientes del servidor
    @GET("outcomingCall")
    Call<ArrayList<PhoneCall>> getOutcomingPhoneCalls();

    //  Subira la llamada saliente al servidor
    @POST("outcomingCall")
    Call<PhoneCall> postOutcomingPhoneCall(@Body PhoneCall llamada);

    //  Actualizara la llamada saliente del servidor
    @PUT("/outcomingCall/{id}")
    @FormUrlEncoded
    Call<PhoneCall> updateOutcomingPhoneCall(@Path("id") long id,
                                    @Field("duration") double duration,
                                    @Field("numberPhone") String numberPhone,
                                    @Field("state") String state,
                                    @Field("time") String time);
}
