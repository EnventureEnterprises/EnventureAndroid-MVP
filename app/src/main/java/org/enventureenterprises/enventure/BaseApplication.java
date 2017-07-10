package org.enventureenterprises.enventure;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.accountkit.AccountKit;

import org.enventureenterprises.enventure.data.model.Migration;
import org.enventureenterprises.enventure.data.remote.AccessToken;
import org.enventureenterprises.enventure.data.remote.EnventureApi;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mossplix on 6/28/17.
 */

public class BaseApplication extends MultiDexApplication {

    private static ApplicationComponent mApplicationComponent;

    public final static String ISSUE_PIC_DIR = "urb";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    private static BaseApplication singleton;
    private Typeface walshFont;


    public static AccessToken accessToken ;
    public static final String PREF_FILE_NAME = "org.enventureenterprises.enventure";

    public static Context context;

    public SharedPreferences preferences;


    @Inject
    EnventureApi mEnventureApi;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        singleton = this;
        context = getApplicationContext();

        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BrandonGrotesqueRegular.ttf")
                .setFontAttrId(org.enventureenterprises.enventure.R.attr.fontPath)
                .build()
        );

        Realm.init(this);



        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("enventure.realm")
                .schemaVersion(4)
                .build();

        try {
            Realm.migrateRealm(realmConfig, new Migration());
        } catch (FileNotFoundException ignored) {
            // If the Realm file doesn't exist, just ignore.
        }


        Realm.setDefaultConfiguration(realmConfig);

        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                super.handleError(e);
                Timber.e(e.toString());
            }
        });

        AccountKit.initialize(this);


        //Realm.deleteRealm(config);
        //Realm.setDefaultConfiguration(config);


        //GeneralUtils.setupTwitterSession(getApplicationContext(),model);

        if ( BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Fabric.with(this, new Crashlytics());
        }


        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        component().inject(this);

    }

    public static BaseApplication getInstance() {
        return singleton;
    }

    public ApplicationComponent component() {
        return mApplicationComponent;
    }


    public ApplicationComponent getActivityComponent() {
        return mApplicationComponent;
    }




    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }


    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getIssueFile(String fileName) {
        // Get the directory for the user's public pictures directory.
        final File picsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final File issuePicsDir = new File(picsDir, ISSUE_PIC_DIR);
        issuePicsDir.mkdirs();
        return new File(issuePicsDir, fileName);
    }

    private void extractWalsh() {
        walshFont = Typeface.createFromAsset(getAssets(), "fonts/gt-walsheim-web.ttf");
    }



    public Typeface getTypeface() {
        if (walshFont == null) {
            extractWalsh();
        }
        return walshFont;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }



    public void setCrashesStatus(boolean status) {
        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("are_crashes_enabled", status);
        editor.apply();
    }



}
