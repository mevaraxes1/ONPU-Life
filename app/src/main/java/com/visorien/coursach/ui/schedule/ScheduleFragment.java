package com.visorien.coursach.ui.schedule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.visorien.coursach.R;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.data.model.schedule.Schedule;
import com.visorien.coursach.ui.subject.SubjectFragment;
import com.visorien.coursach.ui.other.ScheduleItem;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SuppressLint("ValidFragment")
public class ScheduleFragment extends Fragment implements ScheduleMvpView {

    private int day;
    String[] titleDays = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

    RelativeLayout mainView;
//    Toolbar toolbar;
//    Button calendarButton;
    SchedulePresenter presenter;

    @SuppressLint("ValidFragment")
    public ScheduleFragment(int day) {
        super();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        this.day = day+2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new SchedulePresenter(new DataManager(new PreferencesHelper(getActivity().getApplicationContext())));
        presenter.attachView(this);
        mainView = (RelativeLayout) inflater.inflate(R.layout.loading_layout, container, false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(titleDays[day]);
//        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

//        calendarButton = (Button) toolbar.findViewById(R.id.calendar_button);
//        calendarButton.setVisibility(View.VISIBLE);
//        calendarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GregorianCalendar date = new GregorianCalendar();
//                date.setTime(new Date());
//                new DatePickerDialog(getContext(),
//                        datePickerCallback,
//                        date.get(Calendar.YEAR),
//                        date.get(Calendar.MONTH),
//                        date.get(Calendar.DAY_OF_MONTH))
//                        .show();
//            }
//        });
        getSchedule(this.day-1);
        return mainView;
    }
    DatePickerDialog.OnDateSetListener datePickerCallback = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            GregorianCalendar pickerDate = new GregorianCalendar();
            pickerDate.set(Calendar.YEAR, year);
            pickerDate.set(Calendar.MONTH, month);
            pickerDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            getSchedule(pickerDate.get(Calendar.DAY_OF_WEEK)-1);
        }
    };

    private void getSchedule(int day) {
        showLoading();
        try {
            mainView.removeViewAt(1);
        } catch (NullPointerException e) {

        }
        presenter.getSchedule(day);
        Log.d("schedule", " day = "+day);
    }

    @Override
    public void showSchedule(List<Schedule> schedule) {
        Collections.sort(schedule, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return Integer.compare(o1.getSubjectTime(), o2.getSubjectTime());
            }
        });
        Log.d("schedule first", schedule.get(0).getSubjectName());

        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.bg, null));

        TextView dayTitle = new TextView(getContext());
        if(this.day > 0 && this.day <= 7)
            dayTitle.setText(titleDays[this.day-2]);
        dayTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rootLayout.addView(dayTitle);
        //show subjects in view
        int n = 0;
        for (int i = 0; i < schedule.size() + n; i++) {
            if(schedule.get(i-n).getSubjectTime() != i+1) {
                rootLayout.addView(new ScheduleItem(getContext(), i+1));
                n++;
            } else {
                String subtitle = schedule.get(i-n).getSubjectTeacher();
                if(subtitle == null) {
                    subtitle = schedule.get(i-n).getStdGroup();
                }
                ScheduleItem scheduleItem = new ScheduleItem(getContext(), schedule.get(i-n).getSubjectId(), schedule.get(i-n).getSubjectName(), subtitle,
                        schedule.get(i-n).getRoom(), schedule.get(i-n).getSubjectType(), schedule.get(i-n).getSubjectTime());
                scheduleItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScheduleItem item = (ScheduleItem)v;
                        SubjectFragment subjectFragment = new SubjectFragment(item.getId());
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.content_main, subjectFragment).addToBackStack(null);
                        ft.commit();
                    }
                });

                rootLayout.addView(scheduleItem);
            }
        }
        hideLoading();
        mainView.addView(rootLayout);
    }

    @Override
    public void showLoading() {
        View loadingBar = mainView.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        View loadingBar = mainView.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideLoading();
        TextView dayTitle = new TextView(getContext());
        if(this.day-2 >= 0 && this.day-2 <= 6)
            dayTitle.setText(titleDays[this.day-2]);
        dayTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dayTitle.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainView.addView(dayTitle);
        TextView errorText = new TextView(getContext());
        errorText.setText("Расписание не найдено");
        errorText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        errorText.setGravity(Gravity.CENTER);
        mainView.addView(errorText);
    }
}
