package com.visorien.coursach.ui.subject;

import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.model.SimpleAnswer;
import com.visorien.coursach.data.model.subjects.SubjectModel;
import com.visorien.coursach.ui.other.MessageItem;
import com.visorien.coursach.ui.BasePresenter;
import com.visorien.coursach.ui.utils.RxUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Visorien on 28.05.2017.
 */

public class SubjectPresenter extends BasePresenter<SubjectMvpView> {
    private DataManager dataManager;
    private Subscription subscription;

    public SubjectPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void getSubject(int id) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getSubject(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SubjectModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(SubjectModel subjectModel) {
                        getView().showSubject(subjectModel.getSubject());
                    }
                });

    }

    public void deleteMessage(final MessageItem messageItem) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.deleteMessage(messageItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SimpleAnswer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(SimpleAnswer answer) {
                        if(answer.isError()) {
                            getView().showError();
                        } else {
                            getView().updateView();
                        }

                    }
                });

    }
    public void addMessage(int id, String messageText) {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.addMessage(id, messageText)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SimpleAnswer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError();
                    }

                    @Override
                    public void onNext(SimpleAnswer answer) {
                        if(answer.isError()) {
                            getView().showError();
                        } else {
                            getView().updateView();
                        }

                    }
                });

    }

    public String getRole() {
        return dataManager.getRole();
    }
}
