package org.enventureenterprises.enventure.data.remote;


import org.enventureenterprises.enventure.data.model.User;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;



public interface EnventureApiService {



    @POST("/users/convert-token/")
    @FormUrlEncoded
    Observable<Response<AccessToken>> getAccessToken(@Field("client_id") String clientId,
                                                     @Field(value = "token", encoded = true) String token,
                                                     @Field("grant_type") String grant_type,
                                                     @Field("backend") String backend);


    /**
     * Return a list of a users post IDs.
     */
    @GET("/user/{user}")
    Observable<Response<User>> getUser(@Path("user") String user);



    @POST("/users/convert-token/?register=true")
    @FormUrlEncoded
    Observable<Response<AccessToken>>  registerWithTwitter(@Field("client_id") String clientId,
                                                           @Field("token") String token,
                                                           @Field("grant_type") String grantType,
                                                           @Field("backend") String backend,
                                                           @Field("mobile") String mobile,
                                                           @Field("email") String email,
                                                           @Field("newsLetter") Boolean newsLetter);



    @POST("/social_auth/oauth2/token")
    @FormUrlEncoded
    Observable<Response<AccessToken>>  refreshAccessToken(@Field("client_id") String clientId,
                                                          @Field("client_secret") String clientSecret,
                                                          @Field("refresh_token") String refreshToken,
                                                          @Field("grant_type") String grantType);

    @PUT("profile")
    Observable<Response<User>> updateUser(String bio, String mobile, String email);
}
