package org.enventureenterprises.enventure.data.remote;


import org.enventureenterprises.enventure.data.BaseResponse;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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




    @POST("items")
    @Multipart
    Observable<Response<BaseResponse>> createItem(@Part("name") RequestBody name,
                                                 @Part("total_cost") RequestBody total_cost,
                                                 @Part("quantity") RequestBody quantity,
                                                 @Part("mobile") RequestBody mobile,
                                                 @Part("created") RequestBody created,
                                                 @Part MultipartBody.Part image);

    @POST("items")
    @Multipart
    Observable<Response<BaseResponse>> createItem(@Part("name") RequestBody name,
                                                  @Part("total_cost") RequestBody total_cost,
                                                  @Part("quantity") RequestBody quantity,
                                                  @Part("mobile") RequestBody mobile,
                                                  @Part("created") RequestBody created);


    @POST("entries")
    @Multipart
    Observable<Response<BaseResponse>> createEntry( @Part("name") RequestBody name,
                                                   @Part("created") RequestBody created,
                                                   @Part("mobile") RequestBody mobile,
                                                   @Part("amount") RequestBody amount,
                                                   @Part("quantity") RequestBody quantity,
                                                   @Part("type") RequestBody type,
                                                    @Part("customer_mobile") RequestBody customer_mobile,
                                                    @Part("customer_mobile") RequestBody total_price,
                                                    @Part("transaction_type") RequestBody transaction_type

    );


    @POST("entries")
    Observable<Response<BaseResponse>> syncEntry(@Body Entry entry);


    @POST("items")
    Observable<Response<BaseResponse>> syncItem(@Body Item item);


    @GET("entries/{mobile}/")
    Observable<Response<List<Entry>>> getEntries(@Path("mobile") String mobile);

    @GET("entries/{mobile}/{item}/")
    Observable<Response<List<Entry>>> getSales(@Path("mobile") String mobile,@Path("item") String item);

    @GET("entries/{mobile}/{item}/")
    Observable<Response<List<Entry>>> getInventories(@Path("mobile") String mobile,@Path("item") String item);

    @GET("items/{mobile}/")
    Observable<Response<List<Item>>> getItems(@Path("mobile") String mobile);
}
