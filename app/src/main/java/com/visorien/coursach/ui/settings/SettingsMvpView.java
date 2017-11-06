package com.visorien.coursach.ui.settings;

import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.MvpView;

import java.util.List;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface SettingsMvpView extends MvpView {
    void showLogin(String login);
    void updateView();
}
