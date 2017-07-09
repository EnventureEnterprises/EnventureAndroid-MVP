package org.enventureenterprises.enventure.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.util.rx.ApiErrorOperator;
import org.enventureenterprises.enventure.util.rx.Operators;

import io.realm.Realm;
import okhttp3.OkHttpClient;
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





    private @NonNull <T> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(gson);
    }



}
