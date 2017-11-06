package com.visorien.coursach.ui.subject;

import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.MvpView;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface SubjectMvpView extends MvpView {
    void showSubject(Subject subject);
    void updateView();
}
