package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.files.FilesModel;
import com.visorien.coursach.data.model.settings.SettingsModel;
import com.visorien.coursach.data.model.settings.UpdateUserDataRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface SettingsApi {
    @GET("settings/role/{role}/user_id/{user_id}")
    Observable<SettingsModel> getLogin(@Path("role") String role, @Path("user_id") int userId);

    @POST("settings/role/{role}/user_id/{user_id}")
    Observable<SimpleAnswer> updateData(@Path("role") String role, @Path("user_id") int userId,
                                        @Body UpdateUserDataRequest updateRequest);
}
