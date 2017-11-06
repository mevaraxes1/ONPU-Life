package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.schedule.ScheduleModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface ScheduleApi {
    @GET("schedule/role/{role}/day/{day}/teacher_id/{teacher_id}/group/{group}")
    Observable<ScheduleModel> getSchedule(@Path("role") String role, @Path("day") int day, @Path("group") String group, @Path("teacher_id") int teacher_id);
}
