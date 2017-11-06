package com.visorien.coursach.data;

import android.content.SharedPreferences;

import com.visorien.coursach.App;
import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.auth.AuthModel;
import com.visorien.coursach.data.model.auth.User;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.data.model.files.FilesModel;
import com.visorien.coursach.data.model.files.LoadFileModel;
import com.visorien.coursach.data.model.files.RenameFileRequest;
import com.visorien.coursach.data.model.schedule.ScheduleModel;
import com.visorien.coursach.data.model.settings.SettingsModel;
import com.visorien.coursach.data.model.settings.UpdateUserDataRequest;
import com.visorien.coursach.data.model.subjects.AddMessageRequest;
import com.visorien.coursach.data.model.subjects.SubjectModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;
import com.visorien.coursach.data.retrofit.AuthApi;
import com.visorien.coursach.data.retrofit.FilesApi;
import com.visorien.coursach.data.retrofit.ScheduleApi;
import com.visorien.coursach.data.retrofit.SettingsApi;
import com.visorien.coursach.data.retrofit.SubjectApi;
import com.visorien.coursach.data.retrofit.SubjectsApi;
import com.visorien.coursach.ui.other.MessageItem;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;

public class DataManager {

    private final PreferencesHelper preferencesHelper;

    public String getRole() {
        return role;
    }

    private String role;
    private int userId;

    public DataManager(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
        this.role = this.preferencesHelper.getPref().getString("role", "");
        this.userId = this.preferencesHelper.getPref().getInt("id", -1);
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<AuthModel> getUser(String role, String login, String password) {
        return App.getApi().create(AuthApi.class).getUser(role, login, password);
    }

    public Observable<ScheduleModel> getSchedule(int day) {
        String group = "";
        int teacherId = -1;
        if(this.role.equals("student")) {
            group = preferencesHelper.getPref().getString("group", "");
        } else {
            teacherId = preferencesHelper.getPref().getInt("id", -1);
        }
        return App.getApi().create(ScheduleApi.class).getSchedule(role, day, group, teacherId);
    }

    public Observable<SubjectsModel> getSubjects() {
        String group = "";
        int teacherId = -1;
        if(this.role.equals("student")) {
            group = preferencesHelper.getPref().getString("group", "");
        } else {
            teacherId = preferencesHelper.getPref().getInt("id", -1);
        }
        return App.getApi().create(SubjectsApi.class).getSubjects(role, group, teacherId);
    }

    public Observable<SubjectModel> getSubject(int id) {
        return App.getApi().create(SubjectApi.class).getSubject(id);
    }

    //files
    public Observable<FilesModel> getFiles(int id) {
        return App.getApi().create(FilesApi.class).getFiles(id);
    }

    public Observable<SimpleAnswer> deleteFile(int subjectId, int fileId) {
        return App.getApi().create(FilesApi.class).deleteFile(subjectId, fileId);
    }

    public Observable<SimpleAnswer> renameFile(int subjectId, int fileId, String newName) {
        return App.getApi().create(FilesApi.class).renameFile(subjectId, fileId, new RenameFileRequest(newName));
    }

    public Observable<LoadFileModel> openFile(int subjectId, int fileId) {
        return App.getApi().create(FilesApi.class).openFile(subjectId, fileId);
    }

    public Observable<ResponseBody> uploadFile(int subjectId, MultipartBody.Part file) {
        return App.getApi().create(FilesApi.class).upload(String.valueOf(subjectId), file);
    }


    public Observable<SimpleAnswer> deleteMessage(MessageItem messageItem) {
        return App.getApi().create(SubjectApi.class).deleteMessage(messageItem.getMessageId());
    }

    public Observable<SimpleAnswer> addMessage(int id, String messageText) {
        return App.getApi().create(SubjectApi.class).addMessage(new AddMessageRequest(messageText, id));
    }

    public Observable<SettingsModel> getLogin() {
        return App.getApi().create(SettingsApi.class).getLogin(role, userId);
    }

    public Observable<SimpleAnswer> updateUserData(String target, String newValue) {
        return App.getApi().create(SettingsApi.class).updateData(role, userId,
                new UpdateUserDataRequest(target, newValue));
    }

    public void saveUser(String role, User user) {
        SharedPreferences.Editor editor = getPreferencesHelper().getPref().edit();
        editor.putBoolean("isAuthorized", true);
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("role", role);
        this.role = role;
        if(role.equals("student")) {
            editor.putString("group", user.getGroupName());
        }
        editor.commit();
    }

}
