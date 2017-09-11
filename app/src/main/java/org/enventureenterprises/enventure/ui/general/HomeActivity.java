package org.enventureenterprises.enventure.ui.general;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.model.Inventory;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.inventory.InventoryFragment;
import org.enventureenterprises.enventure.ui.reports.ReportsFragment;
import org.enventureenterprises.enventure.ui.sales.SalesFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/6/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigationViewEx.OnNavigationItemSelectedListener {
    @BindView(R.id.bottomNavigation)
    BottomNavigationViewEx bottomNavigation;
    public static final int SALES = 1;
    public static final int INVENTORY = 0;
    public static final int REPORTS = 2;
    public static final int PROFILE = 3;
    private int navTab;
    private int navType = INVENTORY;
    private int currentSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
            supportActionBar.setIcon(
                    new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                navTab = extras.getInt("navTab");


            }

        } else {
            navTab = savedInstanceState.getInt("navTab");


        }


        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.enableAnimation(false);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(false);
        if (savedInstanceState == null) {


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, InventoryFragment.newInstance(), InventoryFragment.TAG)
                    .commit();

        }

        if (navTab == SALES) {
            Menu menu = bottomNavigation.getMenu();
            menu.findItem(R.id.sales).setChecked(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, SalesFragment.newInstance(), SalesFragment.TAG)
                    .commit();
            //bottomNavigation.setCurrentItem(SALES);


        }


    }


    public void onNavigationChanged(int navType) {

        this.navType = navType;
        onModuleChanged(getSupportFragmentManager(), navType);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.inventory:
                onNavigationChanged(INVENTORY);

                break;
            case R.id.sales:


                onNavigationChanged(SALES);


                break;
            case R.id.reports:
                onNavigationChanged(REPORTS);

                break;
            case R.id.profile:
                onNavigationChanged(PROFILE);

                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_container);
        if (fragment instanceof InventoryFragment && fragment != null) {
            ((InventoryFragment) fragment).refreshAdapter();
        }
    }

    @Nullable
    public static Fragment getVisibleFragment(@NonNull FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }


    public void onModuleChanged(@NonNull FragmentManager fragmentManager, int type) {
        Fragment currentVisible = getVisibleFragment(fragmentManager);
        SalesFragment salesFrag = (SalesFragment) fragmentManager.findFragmentByTag(SalesFragment.TAG);
        InventoryFragment inventoryFrag = (InventoryFragment) fragmentManager.findFragmentByTag(InventoryFragment.TAG);
        ReportsFragment reportsFrag = (ReportsFragment) fragmentManager.findFragmentByTag(ReportsFragment.TAG);
        ProfileFragment profileFrag = (ProfileFragment) fragmentManager.findFragmentByTag(ProfileFragment.TAG);

        switch (type) {
            case SALES:
                currentSelected = SALES;

                if (salesFrag == null) {
                    onAddAndHide(fragmentManager, SalesFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, salesFrag, currentVisible);
                }
                break;
            case INVENTORY:
                currentSelected = INVENTORY;
                if (inventoryFrag == null) {
                    onAddAndHide(fragmentManager, InventoryFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, inventoryFrag, currentVisible);
                }
                break;
            case REPORTS:
                currentSelected = REPORTS;
                if (reportsFrag == null) {
                    onAddAndHide(fragmentManager, ReportsFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, reportsFrag, currentVisible);
                }
                break;

            case PROFILE:
                currentSelected = PROFILE;


                if (profileFrag == null) {
                    onAddAndHide(fragmentManager, ProfileFragment.newInstance(), currentVisible);
                } else {
                    onShowHideFragment(fragmentManager, profileFrag, currentVisible);
                }
                break;
        }
    }

    public void onShowHideFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment toShow, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
        toShow.onHiddenChanged(false);
    }

    public void onAddAndHide(@NonNull FragmentManager fragmentManager, @NonNull Fragment toAdd, @NonNull Fragment toHide) {
        toHide.onHiddenChanged(true);
        fragmentManager
                .beginTransaction()
                .hide(toHide)
                .add(R.id.home_container, toAdd, toAdd.getClass().getSimpleName())
                .commit();
        toAdd.onHiddenChanged(false);
    }


    @Override
    public void onBackPressed() {
        if (bottomNavigation.getCurrentItem() != 0) {
            bottomNavigation.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}
