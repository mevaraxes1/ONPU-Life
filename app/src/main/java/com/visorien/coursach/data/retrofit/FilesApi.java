package com.visorien.coursach.data.retrofit;

import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.files.FilesModel;
import com.visorien.coursach.data.model.files.LoadFileModel;
import com.visorien.coursach.data.model.files.RenameFileRequest;
import com.visorien.coursach.data.model.subjects.AddMessageRequest;
import com.visorien.coursach.data.model.subjects.SubjectModel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface FilesApi {
    @GET("files/subject_id/{subject_id}")
    Observable<FilesModel> getFiles(@Path("subject_id") int subjectId);

    @GET("files/subject_id/{subject_id}/file_id/{file_id}")
    Observable<LoadFileModel> openFile(@Path("subject_id") int subjectId, @Path("file_id") int fileId);

    @POST("files/subject_id/{subject_id}/file_id/{file_id}")
    Observable<SimpleAnswer> deleteFile(@Path("subject_id") int subjectId, @Path("file_id") int fileId);

    @POST("files/subject_id/{subject_id}/file_id/{file_id}")
    Observable<SimpleAnswer> renameFile(@Path("subject_id") int subjectId, @Path("file_id") int fileId,
                                        @Body RenameFileRequest renameFileRequest);

    @Multipart
    @POST("files/")
    Observable<ResponseBody> upload(
            @Part("subject_id") String subject_id,
            @Part MultipartBody.Part file
    );
}
