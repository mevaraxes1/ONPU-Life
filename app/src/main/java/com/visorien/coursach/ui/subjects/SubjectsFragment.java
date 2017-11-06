package com.visorien.coursach.ui.subjects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visorien.coursach.R;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.subject.SubjectFragment;
import com.visorien.coursach.ui.other.SubjectsItem;

import java.util.List;

public class SubjectsFragment extends Fragment implements SubjectsMvpView {

    RelativeLayout mainView;
    Toolbar toolbar;
    Button calendarButton;
    SubjectsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        calendarButton = (Button) toolbar.findViewById(R.id.calendar_button);
        calendarButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Предметы");
        presenter = new SubjectsPresenter(new DataManager(new PreferencesHelper(getActivity().getApplicationContext())));
        presenter.attachView(this);

        mainView = (RelativeLayout) inflater.inflate(R.layout.loading_layout, container, false);
        getSubjects();
        return mainView;
    }

    private void getSubjects() {
        showLoading();
        presenter.getSubjects();
    }

    @Override
    public void showSubjects(List<Subject> subjects) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.bg, null));

        for (int i = 0; i < subjects.size(); i++) {
            SubjectsItem subjectsItem = new SubjectsItem(getContext(), subjects.get(i).getId(), subjects.get(i).getTitle(), subjects.get(i).getTeacherName(),
                    subjects.get(i).getExamForm());
            subjectsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubjectsItem item = (SubjectsItem)v;
                    SubjectFragment subjectFragment = new SubjectFragment(item.getId());
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.content_main, subjectFragment).addToBackStack(null);
                    ft.commit();
                }
            });

            rootLayout.addView(subjectsItem);
        }
        hideLoading();
        mainView.addView(rootLayout);
    }

    @Override
    public void showLoading() {
        View loadingBar = mainView.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        View loadingBar = mainView.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideLoading();
        TextView errorText = new TextView(getContext());
        errorText.setText("Предметы не найдены");
        errorText.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        errorText.setGravity(Gravity.CENTER);
        mainView.addView(errorText);
    }
}
