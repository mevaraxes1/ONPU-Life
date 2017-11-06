package com.visorien.coursach.ui.files;

import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.MvpView;

import java.util.List;

/**
 * Created by Visorien on 28.05.2017.
 */

public interface FilesMvpView extends MvpView {
    void showFiles(List<String> files);
    void openFile(String url);
    void updateView();
    void showEmpty();
}
