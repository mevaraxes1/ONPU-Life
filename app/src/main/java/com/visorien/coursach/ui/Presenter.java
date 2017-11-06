package com.visorien.coursach.ui;

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
