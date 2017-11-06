package com.visorien.coursach.ui.subjects;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.auth.AuthModel;
import com.visorien.coursach.data.model.schedule.ScheduleModel;
import com.visorien.coursach.data.model.subjects.SubjectsModel;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.auth.AuthMvpView;
import com.visorien.coursach.ui.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class SubjectsPresenter extends BasePresenter<SubjectsMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public SubjectsPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getSubjects() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getSubjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SubjectsModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(SubjectsModel subjectsModel) {
                        getView().showSubjects(subjectsModel.getSubjects());
                    }
                });

    }
}
