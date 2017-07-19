package org.enventureenterprises.enventure;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;

import net.danlew.android.joda.JodaTimeAndroid;

import org.enventureenterprises.enventure.data.model.Account;
import org.enventureenterprises.enventure.data.model.Migration;
import org.enventureenterprises.enventure.data.remote.AccessToken;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.service.SyncService;
import org.enventureenterprises.enventure.util.PrefUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import jonathanfinerty.once.Once;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mossplix on 6/28/17.
 */

public class BaseApplication extends MultiDexApplication {

    private static ApplicationComponent mApplicationComponent;
    private static final String INIT_SYNC_ON_INSTALL = "org.enventureenterprises.enventure.INIT_SYNC_ON_INSTALL";

    public final static String EVENTURE_PIC_DIR = "enventure";

    private String TASK_TAG ="periodic_task3";
    //private Integer SYNC_INTERVAL = 86400;
    private Integer SYNC_INTERVAL = 100;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    private static BaseApplication singleton;
    private Typeface walshFont;


    public static AccessToken accessToken ;
    public static final String PREF_FILE_NAME = "org.enventureenterprises.enventure";

    public static Context context;

    public SharedPreferences preferences;

    private GcmNetworkManager mGcmNetworkManager;




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

        String mobile = PrefUtils.getMobile(getApplicationContext());
        String databasename ="enventure_p.realm";

        if (mobile != null)
        {
            databasename = mobile+".realm";
        }

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(databasename)


                .schemaVersion(0)
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

        JodaTimeAndroid.init(this);



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
        final File issuePicsDir = new File(picsDir, EVENTURE_PIC_DIR);
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

    private void scheduleSync() {
        if (!Once.beenDone(Once.THIS_APP_INSTALL, INIT_SYNC_ON_INSTALL)) {

            Timber.i("Scheduling background sync on first-install.");
            mGcmNetworkManager = GcmNetworkManager.getInstance(this);

            PeriodicTask task = new PeriodicTask.Builder()
                    .setService(SyncService.class)
                    .setTag(TASK_TAG)
                    .setPeriod(SYNC_INTERVAL)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiredNetwork(PeriodicTask.NETWORK_STATE_CONNECTED )
                    .build();
            setupAccounts();

            mGcmNetworkManager.schedule(task);
            Once.markDone(INIT_SYNC_ON_INSTALL);
        }
    }


    public void setupAccounts()
    {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Account salesAccount = new Account ();
        Account accountReceivable = new Account ();
        Account inventoryAccount = new Account ();
        DateTime d = new DateTime();

        salesAccount.setName(Account.SALES_ACCOUNT);
        salesAccount.setBalance(0.0);
        salesAccount.setQuantity(0);
        salesAccount.setCreated(d.toDate());
        accountReceivable.setName(Account.ACCOUNT_RECEIVABLE);
        accountReceivable.setBalance(0.0);
        accountReceivable.setQuantity(0);
        accountReceivable.setCreated(d.toDate());
        inventoryAccount.setName(Account.INVENTORY_ACCOUNT);
        inventoryAccount.setBalance(0.0);
        inventoryAccount.setQuantity(0);
        inventoryAccount.setCreated(d.toDate());

        realm.copyToRealmOrUpdate (salesAccount);
        realm.copyToRealmOrUpdate (accountReceivable);
        realm.copyToRealmOrUpdate (inventoryAccount);

        realm.commitTransaction();



    }




}
