package org.enventureenterprises.enventure.data.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.data.BaseResponse;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.util.PrefUtils;
import org.enventureenterprises.enventure.util.rx.ApiErrorOperator;
import org.enventureenterprises.enventure.util.rx.Operators;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import io.realm.Realm;
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

    public @NonNull Observable<AccessToken> SyncItems(final @NonNull String fbAccessToken) {

        return service
                .getAccessToken(_clientID, fbAccessToken, "convert_token", "facebook")
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }




    public @NonNull Observable<BaseResponse> createItem(final @NonNull Item item) {
        MultipartBody.Part photo_file = MultipartBody.Part.createFormData ("", "");

        RequestBody name;
        RequestBody total_cost;
        RequestBody quantity;
        RequestBody mobile;
        RequestBody created;
        String mobile_t=PrefUtils.getMobile(mContext);


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
        if(item.getImage() != null) {



            int compressionRatio = 4; //1 == originalImage, 2 = 50% compression, 4=25% compress
            File file = new File (item.getImage());
            RequestBody requestFile;
            Bitmap bitmap;
            BitmapFactory.Options options;
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;


		/* Figure out which way needs to be reduced less */
                int scaleFactor = 1;


		/* Set bitmap options to scale the image decode target */
                options.inSampleSize = scaleFactor;
                options.inPurgeable = true;

                bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(Uri.parse(file.getPath())));


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,compressionRatio, stream);
                byte[] byteArray = stream.toByteArray();
                requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), byteArray);


            }
            catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t.toString ());
                t.printStackTrace ();
                requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), item.getImage());
            }

            photo_file =
                    MultipartBody.Part.createFormData ("picture", item.getName()+".jpg", requestFile);



            return service
                    .createItem(name, total_cost, quantity, mobile,created,photo_file)
                    .lift(apiErrorOperator())
                    .subscribeOn(Schedulers.io());
        }
        else{
            return service
                    .createItem(name, total_cost, quantity, mobile,created)
                    .lift(apiErrorOperator())
                    .subscribeOn(Schedulers.io());
        }









    }

    public @NonNull Observable<BaseResponse> createEntry(final @NonNull Entry entry) {
        RequestBody mobile = RequestBody.create(
                MediaType.parse("multipart/form-data"), PrefUtils.getMobile(mContext));

        RequestBody name;
        RequestBody amount;
        RequestBody quantity;
        RequestBody created;
        RequestBody total_price;
        RequestBody transaction_type;
        RequestBody customer_mobile;

        if (entry.getTotalPrice() != null) {

            total_price =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), entry.getTotalPrice().toString());
        } else {
            total_price =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), "0.0");

        }

        transaction_type =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), entry.getTransactionType());

        if (entry.getCustomerMobile() != null)
        {
            customer_mobile =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), entry.getCustomerMobile());
    }
    else

    {
        customer_mobile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "");

    }




        name =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), entry.getName());

        created =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), entry.getCreated().toString());

        amount =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), entry.getAmount().toString());

        quantity =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), entry.getQuantity().toString());

        RequestBody type =
                RequestBody.create (
                        MediaType.parse ("multipart/form-data"), entry.getType());

        return service
                .createEntry(name, created, mobile, amount,quantity,type,customer_mobile,total_price,transaction_type)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }



    private @NonNull <T> ApiErrorOperator<T> apiErrorOperator() {
        return Operators.apiError(gson);
    }


    public @NonNull Observable<BaseResponse> syncItem(Item item,Realm realm) {
        final int relationshipsDepthLevel = 0;
       Item tosync_item = realm.copyFromRealm(item, relationshipsDepthLevel);

        return service
                .syncItem(tosync_item)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }


    public @NonNull Observable<BaseResponse> syncEntry(Entry entry,Realm realm) {
        final int relationshipsDepthLevel = 2;

        Entry tosync_entry = realm.copyFromRealm(entry, relationshipsDepthLevel);

        return service
                .syncEntry(tosync_entry)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }



    public @NonNull Observable<List<Item>> getItems(final @NonNull String mobile) {

        return service
                .getItems(mobile)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }

    public @NonNull Observable<List<Entry>> getEntries(final @NonNull String mobile) {

        return service
                .getEntries(mobile)
                .lift(apiErrorOperator())
                .subscribeOn(Schedulers.io());
    }






}
