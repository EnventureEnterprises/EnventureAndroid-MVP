package org.enventureenterprises.enventure;

/**
 * Created by mossplix on 6/28/17.
 */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.enventureenterprises.enventure.data.AutoParcelAdapterFactory;
import org.enventureenterprises.enventure.data.CurrentUser;
import org.enventureenterprises.enventure.data.CurrentUserType;
import org.enventureenterprises.enventure.data.DateTimeTypeConverter;
import org.enventureenterprises.enventure.data.Environment;
import org.enventureenterprises.enventure.data.Logout;
import org.enventureenterprises.enventure.data.remote.ApiRequestInterceptor;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.data.remote.EnventureApiService;
import org.enventureenterprises.enventure.data.remote.EnventureRequestInterceptor;
import org.enventureenterprises.enventure.injection.qualifier.AccessTokenPreference;
import org.enventureenterprises.enventure.injection.qualifier.ActivitySamplePreference;
import org.enventureenterprises.enventure.injection.qualifier.ApiRetrofit;
import org.enventureenterprises.enventure.injection.qualifier.ApplicationContext;
import org.enventureenterprises.enventure.injection.qualifier.PackageNameString;
import org.enventureenterprises.enventure.injection.qualifier.UserPreference;
import org.enventureenterprises.enventure.util.Build;
import org.enventureenterprises.enventure.util.Config;
import org.enventureenterprises.enventure.util.PlayServicesCapability;
import org.enventureenterprises.enventure.util.preference.IntPreference;
import org.enventureenterprises.enventure.util.preference.IntPreferenceType;
import org.enventureenterprises.enventure.util.preference.StringPreference;
import org.enventureenterprises.enventure.util.preference.StringPreferenceType;
import org.joda.time.DateTime;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;


    public ApplicationModule(final @NonNull Application application) {
        this.mApplication = application;
    }




    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return Schedulers.computation();
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    AssetManager provideAssetManager() {
        return mApplication.getAssets();
    }



    @Provides
    @Singleton
    CurrentUserType provideCurrentUser(@AccessTokenPreference final @NonNull StringPreferenceType accessTokenPreference,
                                       final @NonNull Gson gson,
                                       @NonNull @UserPreference final StringPreferenceType userPreference) {
        return new CurrentUser(accessTokenPreference, gson, userPreference);
    }


    @Provides
    @Singleton
    @UserPreference
    @NonNull
    StringPreferenceType provideUserPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "user");
    }

    @Provides
    @Singleton
    @NonNull
    EnventureApi provideEnventureApi(final @NonNull EnventureApiService apiService, final @NonNull Gson gson, final @ApplicationContext @NonNull Context context, final @NonNull String clientId) {
        return new EnventureApi(apiService, gson,context,clientId);
    }



    @Provides
    @Singleton
    @NonNull
    Build provideBuild(final @NonNull PackageInfo packageInfo) {
        return new Build(packageInfo);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    @Provides
    @Singleton
    Resources provideResources(final @ApplicationContext @NonNull Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    @PackageNameString
    String providePackageName(final @NonNull Application application) {
        return application.getPackageName();
    }

    @Provides
    @Singleton
    PackageInfo providePackageInfo(final @NonNull Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
                .registerTypeAdapterFactory(new AutoParcelAdapterFactory())
                .create();
    }


    @Provides
    @Singleton
    @NonNull
    PlayServicesCapability providePlayServicesCapability(final @ApplicationContext @NonNull Context context) {
        return new PlayServicesCapability(context);
    }

    @Provides
    @Singleton
    @AccessTokenPreference
    @NonNull StringPreferenceType provideAccessTokenPreference(final @NonNull SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "access_token");
    }


    private @NonNull
    Retrofit createRetrofit(final @NonNull HttpUrl apiEndpoint, final @NonNull Gson gson, final @NonNull OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiEndpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();


    }

    @Provides
    @Singleton
    @NonNull
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return interceptor;
    }

    @Provides
    @Singleton
    @NonNull
    EnventureRequestInterceptor provideEnventureRequestInterceptor(final @NonNull Build build) {
        return new EnventureRequestInterceptor(build);
    }

    @Provides
    @Singleton
    String provideClientId(final @NonNull Build build) {
        if( build.isDebug()){

            return Config.DEBUG_CLIENT_ID;

        }
        else{

            return Config.CLIENT_ID;

        }



    }

    @Provides
    @Singleton
    HttpUrl provideApiEndpoint(final @NonNull Build build) {
        if(build.isDebug()){

            return HttpUrl.parse(Config.DebugBaseEndpoint);

        }
        else{

            return HttpUrl.parse(Config.BaseEndpoint);

        }



    }

    @Provides
    @Singleton
    @NonNull
    EnventureApiService provideApiService(final @ApiRetrofit @NonNull Retrofit apiRetrofit) {
        return apiRetrofit.create(EnventureApiService.class);
    }


    @Provides
    @Singleton
    @ApiRetrofit
    @NonNull Retrofit provideApiRetrofit(final @NonNull HttpUrl apiEndpoint,
                                         final @NonNull Gson gson,
                                         final @NonNull OkHttpClient okHttpClient) {
        return createRetrofit(apiEndpoint, gson, okHttpClient);
    }

    @Provides
    @Singleton
    @NonNull
    ApiRequestInterceptor provideApiRequestInterceptor(final @NonNull String clientId,
                                                       final @NonNull CurrentUserType currentUser, final @NonNull HttpUrl apiEndpoint) {
        return new ApiRequestInterceptor(clientId, currentUser, apiEndpoint.toString());
    }

    @Provides
    @Singleton
    @ActivitySamplePreference
    @NonNull
    IntPreferenceType provideActivitySamplePreference(final @NonNull SharedPreferences sharedPreferences) {
        return new IntPreference(sharedPreferences, "last_seen_activity_id");
    }



    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideOkHttpClient(final @NonNull ApiRequestInterceptor apiRequestInterceptor,
                                     final @NonNull HttpLoggingInterceptor httpLoggingInterceptor, final @NonNull EnventureRequestInterceptor enventureRequestInterceptor,
                                     final @NonNull Build build) {

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Only log in debug mode to avoid leaking sensitive information.
        if (build.isDebug()) {
            builder.addInterceptor(httpLoggingInterceptor);
        }




        return builder
                .addInterceptor(apiRequestInterceptor)
                .addInterceptor(enventureRequestInterceptor)
                .build();
    }


    @Provides
    @Singleton
    Logout provideLogout(final @NonNull CurrentUserType currentUser) {
        return new Logout( currentUser);
    }


    @Provides
    @Singleton
    Environment provideEnvironment(@NonNull EnventureApi apiClient,
                                   final @NonNull CurrentUserType currentUser,
                                   final @NonNull Gson gson,

                                   final @NonNull Scheduler scheduler,
                                   final @NonNull SharedPreferences sharedPreferences
    ) {

        return Environment.builder()
                .apiClient(apiClient)
                .currentUser(currentUser)
                .gson(gson)
                .scheduler(scheduler)
                .sharedPreferences(sharedPreferences)
                .build();
    }




}
