package org.enventureenterprises.enventure.data;

import android.support.annotation.NonNull;

import com.facebook.accountkit.AccountKit;

/**
 * Created by mossplix on 4/29/17.
 */

public class Logout {
    private final CurrentUserType currentUser;

    public Logout(final @NonNull CurrentUserType currentUser) {
        this.currentUser = currentUser;
    }

    public void execute() {
        currentUser.logout();


            AccountKit.logOut();
       

       
    }
}
