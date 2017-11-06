package com.visorien.coursach.ui.settings;

import android.util.Log;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.settings.SettingsModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.subjects.SubjectsMvpView;
import com.visorien.coursach.ui.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class SettingsPresenter extends BasePresenter<SettingsMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public SettingsPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getLogin() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getLogin()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SettingsModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SettingsModel settingsModel) {
                        if (!settingsModel.isError())
                            getView().showLogin(settingsModel.getLogin());
                        else
                            getView().showError();
                    }
                });

    }
    public void updateData(String target, String newValue) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.updateUserData(target, newValue)
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
                        if (!answer.isError())
                            getView().updateView();
                        else
                            getView().showError();
                    }
                });

    }
}
