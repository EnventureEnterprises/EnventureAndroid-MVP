package org.enventureenterprises.enventure.ui.base;

/**
 * Created by mossplix on 6/28/17.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.enventureenterprises.enventure.ApplicationComponent;
import org.enventureenterprises.enventure.ApplicationGraph;
import org.enventureenterprises.enventure.BaseApplication;
import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.Environment;
import org.enventureenterprises.enventure.data.local.SectionsPagerAdapter;
import org.enventureenterprises.enventure.data.model.Account;
import org.enventureenterprises.enventure.data.model.Entry;
import org.enventureenterprises.enventure.data.model.Item;
import org.enventureenterprises.enventure.data.model.WeeklyReport;
import org.enventureenterprises.enventure.data.remote.EnventureApi;
import org.enventureenterprises.enventure.ui.general.HomeActivity;
import org.enventureenterprises.enventure.util.PrefUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener, LifecycleProvider<ActivityEvent> {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar mActionBarToolbar;
    private String TASK_TAG ="periodic_task2";
    //private Integer SYNC_INTERVAL = 86400;
    private Integer SYNC_INTERVAL = 30;

    private static final int REQUEST_INVITE = 30;
    private Subscription subscription;
    @Inject
    EnventureApi client;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    protected static final int NAVDRAWER_ITEM_INVALID = -1;

    private final PublishSubject<Void> back = PublishSubject.create();

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private ApplicationGraph mApplicationGraph;

    private GcmNetworkManager mGcmNetworkManager;

    private Realm realm;
    RealmResults<Entry> entries;
    RealmResults<Item> items;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        getActivityComponent().inject(this);
        PrefUtils.setBoolean(getApplicationContext(), "sync", true);





        String mobile = PrefUtils.getMobile(getApplicationContext());

        if (mobile != null) {



        realm = Realm.getDefaultInstance();


        if(realm != null) {
            items = realm.where(Item.class).findAllAsync();
            entries = realm.where(Entry.class).findAllAsync();

            entries.addChangeListener(new RealmChangeListener<RealmResults<Entry>>() {
                @Override
                public void onChange(RealmResults<Entry> entries) {
                    Entry.updateReports(new DateTime());
                }
            });


            items.addChangeListener(new RealmChangeListener<RealmResults<Item>>() {
                @Override
                public void onChange(RealmResults<Item> items) {
                    Entry.updateReports(new DateTime());
                }
            });
        }
        }

        getitems();
    }





    /**
     * Override in subclasses for custom exit transitions. First item in pair is the enter animation,
     * second item in pair is the exit animation.
     */
    protected @Nullable
    Pair<Integer, Integer> exitTransition() {
        return null;
    }


    protected final void startActivityWithTransition(final @NonNull Intent intent, final @AnimRes int enterAnim,
                                                     final @AnimRes int exitAnim) {
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }




    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    /**
     * Enables back navigation for activities that are launched from the NavBar. See
     * {@code AndroidManifest.xml} to find out the parent activity names for each activity.
     * @param intent
     */
    private void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
        } else {
            startActivity(intent);
            //finish();
        }
    }


    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mActionBarToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
                mActionBarToolbar.setNavigationContentDescription(getResources().getString(R.string
                        .navdrawer_description_a11y));
                setSupportActionBar(mActionBarToolbar);
                ActionBar actionBar = getSupportActionBar();

                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);



            }
        }
        return mActionBarToolbar;
    }



    /**
     * This utility method handles Up navigation intents by searching for a parent activity and
     * navigating there if defined. When using this for an activity make sure to define both the
     * native parentActivity as well as the AppCompat one when supporting API levels less than 16.
     * when the activity has a single parent activity. If the activity doesn't have a single parent
     * activity then don't define one and this method will use back button functionality. If "Up"
     * functionality is still desired for activities without parents then use
     * {@code syntheticParentActivity} to define one dynamically.
     *
     * Note: Up navigation intents are represented by a back arrow in the top left of the Toolbar
     *       in Material Design guidelines.
     *
     * @param currentActivity Activity in use when navigate Up action occurred.
     * @param syntheticParentActivity Parent activity to use when one is not already configured.
     */
    public static void navigateUpOrBack(Activity currentActivity,
                                        Class<? extends Activity> syntheticParentActivity) {
        // Retrieve parent activity from AndroidManifest.
        Intent intent = NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if (intent == null && syntheticParentActivity != null) {
            try {
                intent = NavUtils.getParentActivityIntent(currentActivity, syntheticParentActivity);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (intent == null) {
            // No parent defined in manifest. This indicates the activity may be used by
            // in multiple flows throughout the app and doesn't have a strict parent. In
            // this case the navigation up button should act in the same manner as the
            // back button. This will result in users being forwarded back to other
            // applications if currentActivity was invoked from another application.
            currentActivity.onBackPressed();
        } else {
            if (NavUtils.shouldUpRecreateTask(currentActivity, intent)) {
                // Need to synthesize a backstack since currentActivity was probably invoked by a
                // different app. The preserves the "Up" functionality within the app according to
                // the activity hierarchy defined in AndroidManifest.xml via parentActivity
                // attributes.
                TaskStackBuilder builder = TaskStackBuilder.create(currentActivity);
                builder.addNextIntentWithParentStack(intent);
                builder.startActivities();
            } else {
                // Navigate normally to the manifest defined "Up" activity.
                NavUtils.navigateUpTo(currentActivity, intent);
            }
        }
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of BaseActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }



    private void populateNavDrawer() {

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.add_entry:
                startActivityWithTransition(new Intent(getApplicationContext(), HomeActivity.class), R.anim.slide_in_right, R.anim.fade_out_slide_out_left);
                break;
            /*case R.id.nav_notifs:

                break;
              case R.id.nav_profile:
                createBackStack(new Intent(getApplicationContext(), ProfileActivity.class));
                break;*/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }




    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();


    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    protected @NonNull
    BaseApplication application() {
        return (BaseApplication) getApplication();
    }






    /**
     * Convenience method to return a Dagger component.
     */
    protected @NonNull
    ApplicationComponent component() {
        return application().component();
    }

    public  @NonNull ApplicationComponent getActivityComponent(){
        return application().component();

    }



    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
         realm = Realm.getDefaultInstance();
        lifecycleSubject.onNext(ActivityEvent.START);
        back
                .compose(bindUntilEvent(ActivityEvent.STOP))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> goBack());
    }


    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni == null) || (!ni.isConnected())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.device_offline_message),
                    Toast.LENGTH_LONG).show();
        }
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);

        if(realm != null) {
            getRealm().removeAllChangeListeners();
        }
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        closeRealm();
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }


    /**
     * Call when the user wants triggers a back event, e.g. clicking back in a toolbar or pressing the device back button.
     */
    public void back() {
        back.onNext(null);
    }


    protected Realm getRealm() {
        return realm;
    }



    private void closeRealm() {
        if (realm != null) {
            realm.removeAllChangeListeners();
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            realm.close();
            realm = null;
        }
    }




    /**
     * Triggers a back press with an optional transition.
     */
    private void goBack() {
        super.onBackPressed();

        final Pair<Integer, Integer> exitTransitions = exitTransition();
        if (exitTransitions != null) {
            overridePendingTransition(exitTransitions.first, exitTransitions.second);
        }
    }

    protected @NonNull
    Environment environment() {
        return component().environment();
    }



    public void setupAccounts()
    {

        Realm realm = getRealm();
        getRealm().beginTransaction();
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


    public void getitems() {
        Realm realm = getRealm();
        if( PrefUtils.getBoolean(getApplicationContext(),"sync")) {
            client.getItems(PrefUtils.getMobile(getApplicationContext()))
                    .subscribe(new Action1<List<Item>>() {
                                   @Override
                                   public void call(List<Item> items) {
                                       realm.executeTransactionAsync (new Realm.Transaction () {
                                                                          @Override
                                                                          public void execute(Realm realm) {
                                                                              for (Item item : items) {
                                                                                  realm.beginTransaction();
                                                                                  DateTime d = new DateTime(item.getCreated());
                                                                                  Entry nEntry = realm.createObject(Entry.class, d.getMillis());
                                                                                  String day_name = d.toString(DateTimeFormat.mediumDate().withLocale(Locale.getDefault()).withZoneUTC());
                                                                                  String week_name =  WeeklyReport.getWeekName(d);
                                                                                  String month_name = d.toString("MMM-Y");


                                                                                  nEntry.setQuantity(item.getQuantity());
                                                                                  nEntry.setAmount(item.getAmount());
                                                                                  nEntry.setSynced(false);
                                                                                  nEntry.setCreated(d.toDateTime().toDate());
                                                                                  nEntry.setEntryMonth(month_name);
                                                                                  nEntry.setEntryWeek(week_name);
                                                                                  nEntry.setEntryDay(day_name);
                                                                                  nEntry.setCreated(item.getCreated());
                                                                                  nEntry.setTransactionType("inventory");
                                                                                  item.addInventoryUpdate(nEntry);
                                                                                  realm.copyToRealmOrUpdate(nEntry);

                                                                                  realm.copyToRealmOrUpdate(item);
                                                                                  realm.commitTransaction();
                                                                              }
                                                                          }
                                                                      },
                                               new Realm.Transaction.OnError () {
                                                   @Override
                                                   public void onError(Throwable throwable) {
                                                       Timber.e (throwable, "Could not save data");
                                                   }
                                               }
                                       );

                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Timber.d("Failure: Items  Data not loaded:- %s", throwable.toString());
                                }
                            });


            client.getEntries(PrefUtils.getMobile(getApplicationContext()))
                    .subscribe(new Action1<List<Entry>>() {
                                   @Override
                                   public void call(List<Entry> entries) {



                                      realm.executeTransactionAsync (new Realm.Transaction () {
                                                                          @Override
                                                                          public void execute(Realm realm) {
                                                                              for (Entry entry : entries) {

                                                                                  Entry.newEntry(entry);
                                                                              }
                                                                          }
                                                                      },
                                               new Realm.Transaction.OnError () {
                                                   @Override
                                                   public void onError(Throwable throwable) {
                                                       Timber.e (throwable, "Could not save data");
                                                   }
                                               }
                                       );

                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Timber.d("Failure: Entries  Data not loaded:- %s", throwable.toString());
                                }
                            });
            //PrefUtils.setBoolean(getApplicationContext(), "sync", false);
        }
}
}







