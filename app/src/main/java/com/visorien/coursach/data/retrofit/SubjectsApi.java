package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.schedule.ScheduleModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface SubjectsApi {
    @GET("subjects/role/{role}/teacher_id/{teacher_id}/group/{group}")
    Observable<SubjectsModel> getSubjects(@Path("role") String role, @Path("group") String group, @Path("teacher_id") int teacher_id);
}
