package com.visorien.coursach.ui.files;

import android.util.Log;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.files.FilesModel;
import com.visorien.coursach.data.model.files.LoadFileModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.utils.RxUtil;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class FilesPresenter extends BasePresenter<FilesMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public FilesPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getFiles(int id) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getFiles(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FilesModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(FilesModel filesModel) {
                        if(filesModel.getFiles() != null)
                            getView().showFiles(filesModel.getFiles());
                        else
                            getView().showEmpty();
                    }
                });

    }

    public void openFile(int subjectId, int fileId) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.openFile(subjectId, fileId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoadFileModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(LoadFileModel file) {
                        if(!file.isError())
                            getView().openFile(file.getPath());
                    }
                });

    }

    public void deleteFile(int subjectId, int fileId) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.deleteFile(subjectId, fileId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SimpleAnswer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().updateView();
                    }

                    @Override
                    public void onNext(SimpleAnswer answer) {
                        getView().updateView();
                    }
                });

    }

    public void renameFile(int subjectId, int fileId, String newName) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.renameFile(subjectId, fileId, newName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SimpleAnswer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().updateView();
                    }

                    @Override
                    public void onNext(SimpleAnswer answer) {
                            getView().updateView();
                    }
                });

    }

    public void uploadFile(int subjectId, MultipartBody.Part file) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.uploadFile(subjectId, file)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().updateView();
                    }

                    @Override
                    public void onNext(ResponseBody answer) {
                            getView().updateView();
                    }
                });

    }

    public String getRole() {
        return dataManager.getRole();
    }
}
