package com.visorien.coursach.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visorien.coursach.R;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;

/**
 * Created by Visorien on 24.05.2017.
 */

public class SettingsFragment extends Fragment implements SettingsMvpView {

    RelativeLayout mainLayout;
    Toolbar toolbar;
    Button calendarButton;
    View rootLayout;
    SettingsPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Настройки");
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        calendarButton = (Button) toolbar.findViewById(R.id.calendar_button);
        calendarButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new SettingsPresenter(new DataManager(new PreferencesHelper(getActivity().getApplicationContext())));
        presenter.attachView(this);

        mainLayout = (RelativeLayout) inflater.inflate(R.layout.loading_layout, container, false);
        rootLayout = inflater.inflate(R.layout.settings, container, false);
        presenter.getLogin();
        return mainLayout;
    }

    private void makeSettingsView(String login) {

    }

    private void updateData(final String target) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Новое значение");
        final EditText input = new EditText(getContext());
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String newName = input.getEditableText().toString();
                presenter.updateData(target, newName);
            }
        });
        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        hideLoading();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void showLogin(String login) {
        TextView loginField = (TextView) rootLayout.findViewById(R.id.login_field);
        loginField.setText(login);
        Button changeLogin = (Button) rootLayout.findViewById(R.id.change_login_button);
        Button changePassword = (Button) rootLayout.findViewById(R.id.change_password_button);
        changeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData("login");
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData("password");
            }
        });
        hideLoading();
        mainLayout.addView(rootLayout);
    }

    @Override
    public void updateView() {
        showLoading();
        presenter.getLogin();
        try {
            Toast.makeText(getContext(), "Изменения применены", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {
        View loadingBar = mainLayout.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        View loadingBar = mainLayout.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideLoading();
        try {
            Toast.makeText(getContext(), "Ошибка, попробуйте еще раз", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
