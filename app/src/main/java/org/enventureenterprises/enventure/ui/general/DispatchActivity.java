package org.enventureenterprises.enventure.ui.general;

import android.content.Intent;
import android.os.Bundle;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.CurrentUserType;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.signin.LoginActivity;

/**
 * Created by mossplix on 7/7/17.
 */

public class DispatchActivity extends BaseActivity {

    private CurrentUserType currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        currentUser = environment().currentUser();

        if(currentUser.getAccessToken() != null)
        {
            goToHome();
        }
        else{
            goToLogin();
            //goToHome();
        }



/*
        currentUser.loggedInUser()
                .take(1)
                .compose(bindToLifecycle())
                .subscribe(__ -> goToHome());

        currentUser.isLoggedIn()
                .map(loggedIn -> !loggedIn)
                .compose(bindToLifecycle())
                .subscribe(__ -> goToLogin());*/



    }

    public void goToLogin() {
        final Intent loginIntent = new Intent(DispatchActivity.this, LoginActivity.class);
        startActivityWithTransition(loginIntent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
        finish();

    }

    public void goToHome() {
        final Intent intent = new Intent(DispatchActivity.this, HomeActivity.class);
        startActivityWithTransition(intent, R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
        finish();
    }
}

