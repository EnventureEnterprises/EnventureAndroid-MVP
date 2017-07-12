package org.enventureenterprises.enventure.data;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

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


        FirebaseAuth.getInstance().signOut();
       

       
    }
}
