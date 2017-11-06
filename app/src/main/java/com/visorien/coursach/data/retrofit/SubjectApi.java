package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.subjects.AddMessageRequest;
import com.visorien.coursach.data.model.subjects.SubjectModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface SubjectApi {
    @GET("subject/id/{id}")
    Observable<SubjectModel> getSubject(@Path("id") int id);

    @POST("subject/message_id/{message_id}")
    Observable<SimpleAnswer> deleteMessage(@Path("message_id") int messageId);

    @POST("subject")
    Observable<SimpleAnswer> addMessage(@Body AddMessageRequest addMessageRequest);
}
