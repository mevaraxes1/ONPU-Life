package com.visorien.coursach.ui.subjects;

import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.MvpView;

import java.util.List;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface SubjectsMvpView extends MvpView {
    void showSubjects(List<Subject> subjects);
}
