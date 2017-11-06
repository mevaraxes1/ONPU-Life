package com.visorien.coursach.ui.auth;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.auth.AuthModel;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class AuthPresenter  extends BasePresenter<AuthMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public AuthPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getUser(final String role, String login, String password) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getUser(role, login, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AuthModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(AuthModel authModel) {
                        getView().authSuccess();
                        dataManager.saveUser(role, authModel.getUser());
                    }
                });

    }
}
