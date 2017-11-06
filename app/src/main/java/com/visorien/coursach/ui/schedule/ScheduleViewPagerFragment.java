package com.visorien.coursach.ui.schedule;

import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.visorien.coursach.R;

import java.util.Calendar;
import java.util.Date;

public class ScheduleViewPagerFragment extends Fragment {

    private static final int PAGE_COUNT = 7;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private RelativeLayout mainLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Расписание");
        mainLayout = (RelativeLayout) inflater.inflate(R.layout.schedule_pager_fragment, container, false);
        pager = (ViewPager) mainLayout.findViewById(R.id.schedule_pager);
        pagerAdapter = new SchedulePagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        calendar.setTime(new Date());
        pager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK)-2);
        return mainLayout;
    }

    private class SchedulePagerAdapter extends FragmentPagerAdapter {

        public SchedulePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("pager position", " "+position);
            return new ScheduleFragment(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
