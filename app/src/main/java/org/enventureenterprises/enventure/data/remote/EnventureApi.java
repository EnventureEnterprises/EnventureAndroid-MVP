package org.enventureenterprises.enventure.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.data.BaseResponse;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.util.rx.ApiErrorOperator;
import org.enventureenterprises.enventure.util.rx.Operators;

import java.util.Date;

import io.realm.Realm;
import io.realm.annotations.PrimaryKey;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;


/**
 * Created by Moses on 7/22/16.
 */

public class EnventureApi  {

    private EnventureApiService service;
    private  Gson gson;
    private String _clientID;


    private
    @ApplicationContext
    Context mContext;

    private Realm realm;
    private BehaviorSubject<Boolean> networkInUse;

    private OkHttpClient httpClient;


    public EnventureApi(final @NonNull EnventureApiService service, final @NonNull Gson gson, final @ApplicationContext @NonNull Context context, final  @NonNull String clientId) {
        this.gson = gson;
        this.service = service;
        this.mContext = context;
        this._clientID=clientId;
    }




    public @NonNull Observable<AccessToken> loginWithFacebook(final @NonNull String fbAccessToken, final @NonNull String code) {
        String token = "oauth_token=" + fbAccessToken + "%26oauth_token_secret=" + code;
        return service
                .getAccessToken(_clientID, token, "convert_token", "facebook")
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    public @NonNull Observable<AccessToken> loginWithFacebook(final @NonNull String fbAccessToken) {

        return service
                .getAccessToken(_clientID, fbAccessToken, "convert_token", "facebook")
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    private  long id;
    private Date created;
    private Double amount;
    private Double totalCost;
    private Integer quantity;
    private String image;
    @PrimaryKey
    private String name;


    public @NonNull Observable<BaseResponse> createItem(final @NonNull Item item) {
        MultipartBody.Part photo_file = MultipartBody.Part.createFormData ("", "");

        RequestBody name;
        RequestBody total_cost;
        RequestBody quantity;
        RequestBody mobile;
        RequestBody created;
        String mobile_t="";


        name =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), item.getName());

        created =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), item.getCreated().toString());

        total_cost =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), item.getTotalCost().toString());

        quantity =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), item.getQuantity().toString());

        mobile =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), mobile_t);

        RequestBody requestFile =
                RequestBody.create (MediaType.parse ("multipart/form-data"), item.getImage());

        photo_file =
                MultipartBody.Part.createFormData ("picture", "img", requestFile);



        return service
                .createItem(name, total_cost, quantity, mobile,created,photo_file)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());

    }

    public @NonNull Observable<BaseResponse> createEntry(final @NonNull Entry entry) {
        String mobile = "";

        return service
                .createEntry(entry.getName(), entry.getCreated(), mobile, entry.getAmount(),entry.getQuantity(),entry.getType())
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }



    private @NonNull <T> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(gson);
    }



}
