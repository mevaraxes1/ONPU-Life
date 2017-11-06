package com.visorien.coursach.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.visorien.coursach.R;
import com.visorien.coursach.ui.MainActivity;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;

public class AuthActivity extends AppCompatActivity implements AuthMvpView {

    Button loginButton;
    EditText loginField;
    EditText passwordField;
    RadioGroup isStudentRadio;
    AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        loginField = (EditText) findViewById(R.id.login_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        isStudentRadio = (RadioGroup) findViewById(R.id.is_student_radio);

        presenter = new AuthPresenter(new DataManager(new PreferencesHelper(getApplicationContext())));
        presenter.attachView(this);

        // Login button Click Event
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String login = loginField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                int radioChooseId = isStudentRadio.getCheckedRadioButtonId();
                String role = radioChooseId == R.id.radio_student ? "student" : "teacher";

                // Check for empty data in the form
                if (!login.isEmpty() && !password.isEmpty()) {
                    // login user
                    presenter.getUser(role, login, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
    }

    @Override
    public void authSuccess() {
        //open main activity
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(),
                "Ошибка при попытке входа, попробуйсте еще раз", Toast.LENGTH_LONG)
                .show();
    }
}
