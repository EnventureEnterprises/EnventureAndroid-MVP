package org.enventureenterprises.enventure.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.enventureenterprises.enventure.data.model.User;
import org.enventureenterprises.enventure.util.preference.StringPreferenceType;

import io.realm.Realm;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;


/**
 * Created by mossplix on 4/28/17.
 */

public class CurrentUser extends CurrentUserType {
    private final StringPreferenceType accessTokenPreference;
    private final StringPreferenceType userPreference;

    private final BehaviorSubject<User> user = BehaviorSubject.create();

    public CurrentUser(final @NonNull StringPreferenceType accessTokenPreference,
                       final @NonNull Gson gson,
                       final @NonNull StringPreferenceType userPreference) {
        this.accessTokenPreference = accessTokenPreference;
        this.userPreference = userPreference;

        user
                .skip(1)
                .filter(user -> user != null)
                .subscribe(user -> userPreference.set(gson.toJson(user, User.class)));

        user.onNext(gson.fromJson(userPreference.get(), User.class));
    }

    @Override
    public @Nullable
    User getUser() {
        return user.getValue();
    }

    @Override
    public boolean exists() {
        return getUser() != null;
    }

    public String getAccessToken() {
        return accessTokenPreference.get();
    }

      @Override
    public void login(final @NonNull String accessToken) {
          accessTokenPreference.set(accessToken);

          Realm realm2 = Realm.getDefaultInstance ();

          User newUser = new User();

          realm2.beginTransaction();



          realm2.copyToRealmOrUpdate (newUser);

          realm2.commitTransaction();
          realm2.close();
          user.onNext(newUser);


    }

    @Override
    public void login(final @NonNull User newUser, final @NonNull String accessToken) {
        Timber.d("Login user %s", newUser.getFirstName());

        accessTokenPreference.set(accessToken);
        Realm realm2 = Realm.getDefaultInstance ();

        realm2.beginTransaction();
        newUser.setMe (true);


        realm2.copyToRealmOrUpdate (newUser);

        realm2.commitTransaction();
        realm2.close();
        user.onNext(newUser);
        //deviceRegistrar.registerDevice();
    }

    @Override
    public void logout() {
        Timber.d("Logout current user");

        userPreference.delete();
        accessTokenPreference.delete();


        user.onNext(null);
        //deviceRegistrar.unregisterDevice();
    }

    @Override
    public void refresh(final @NonNull User freshUser) {
        user.onNext(freshUser);
    }



    @Override
    public Observable<User> observable() {
        return user;
    }
}
