package org.enventureenterprises.enventure.ui.reports;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import org.enventureenterprises.enventure.R;
import org.enventureenterprises.enventure.data.local.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mossplix on 7/7/17.
 */

public class ReportsFragment extends BaseFragment {
    public static final String TAG = "reports_fragment";
    private int tabPosition = 0;

    Stack<Integer> stack;

    @BindView(R.id.vpPager)
    ViewPager mViewPager;


    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<String> frags = Lists.newArrayList("Daily", "Monthly", "Weekly");
    private SectionsPagerAdapter mSectionsPagerAdapter;



    public static ReportsFragment newInstance() {
        ReportsFragment fragment = new ReportsFragment();
        return fragment;
    }

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reports_fragment, container, false);
        ButterKnife.bind(this, view);
        stack = new Stack<Integer>();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), frags);


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabs.setupWithViewPager(mViewPager);



        //tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        //applyFont(this, mViewPager, tabs);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                mViewPager.setCurrentItem(tab.getPosition());

                
                if (stack.empty())
                    stack.push(0);

                if (stack.contains(tabPosition)) {
                    stack.remove(stack.indexOf(tabPosition));
                    stack.push(tabPosition);
                } else {
                    stack.push(tabPosition);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                //tabPositionUnselected = tab.getPosition();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        return view;
    }
}
