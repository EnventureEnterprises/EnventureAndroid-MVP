package org.enventureenterprises.enventure.ui.general;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.ui.base.BaseActivity;
import org.enventureenterprises.enventure.ui.inventory.InventoryFragment;
import org.enventureenterprises.enventure.ui.reports.ReportsFragment;
import org.enventureenterprises.enventure.ui.sales.SalesFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * Created by mossplix on 7/6/17.
 */

public class HomeActivity extends BaseActivity implements BottomNavigation.OnMenuItemSelectionListener {
    @BindView(R.id.bottomNavigation) BottomNavigation bottomNavigation;
    private static final int SALES = 1;
    private static final int INVENTORY =0;
    private static final int REPORTS = 2;
    private static final int PROFILE = 3;
    private int navType =SALES;
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
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigation.setOnMenuItemClickListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_container, InventoryFragment.newInstance(), InventoryFragment.TAG)
                    .commit();
        }




    }


    public void onNavigationChanged(int navType) {
        //noinspection WrongConstant
        if (bottomNavigation.getSelectedIndex() != navType) bottomNavigation.setSelectedIndex(navType, true);
        this.navType = navType;
        onModuleChanged(getSupportFragmentManager(), navType);
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
        SalesFragment salesFrag = (SalesFragment)fragmentManager.findFragmentByTag(SalesFragment.TAG);
        InventoryFragment inventoryFrag = (InventoryFragment)fragmentManager.findFragmentByTag(InventoryFragment.TAG);
        ReportsFragment reportsFrag = (ReportsFragment)fragmentManager.findFragmentByTag(ReportsFragment.TAG);
        ProfileFragment profileFrag = (ProfileFragment)fragmentManager.findFragmentByTag(ProfileFragment.TAG);

        switch (type) {
            case SALES:
                currentSelected =SALES;
                if (salesFrag== null) {
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


    @Override public void onMenuItemSelect(@IdRes int id, int position, boolean fromUser) {

        onNavigationChanged(position);

    }

    @Override public void onMenuItemReselect(@IdRes int id, int position, boolean fromUser) {}





}
