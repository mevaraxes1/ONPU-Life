package com.visorien.coursach.ui.schedule;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.auth.AuthModel;
import com.visorien.coursach.data.model.schedule.ScheduleModel;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.MvpView;
import com.visorien.coursach.ui.auth.AuthMvpView;
import com.visorien.coursach.ui.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class SchedulePresenter extends BasePresenter<ScheduleMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public SchedulePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getSchedule(int day) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getSchedule(day)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ScheduleModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(ScheduleModel scheduleModel) {
                        if(!scheduleModel.isError()) {
                            getView().showSchedule(scheduleModel.getSchedule());
                        } else {
                            getView().showError();
                        }
                    }
                });

    }
}
