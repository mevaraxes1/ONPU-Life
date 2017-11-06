package com.visorien.coursach.ui.schedule;

import com.visorien.coursach.data.model.schedule.Schedule;
import com.visorien.coursach.ui.MvpView;

import java.util.List;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface ScheduleMvpView extends MvpView {
    void showSchedule(List<Schedule> schedule);
}
