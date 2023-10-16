package com.calferinnovate.mediconnecta.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ui.login.LoginViewModel;
import com.calferinnovate.mediconnecta.ui.login.LoginViewModelFactory;
import com.calferinnovate.mediconnecta.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ImageView logoLogin;
    private TextInputLayout userLabel;
    private TextInputEditText user;
    private TextInputLayout passLabel;
    private TextInputEditText pass;

private View progrssView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logoLogin = (ImageView) findViewById(R.id.logo);
        userLabel = (TextInputLayout) findViewById(R.id.username);
        user = (TextInputEditText) findViewById(R.id.username);
        passLabel = (TextInputLayout) findViewById(R.id.password);
        pass = (TextInputEditText) findViewById(R.id.password);
        Button botonAcceso = (Button) findViewById(R.id.login);
        progrssView = findViewById(R.id.loading);

        // Setup
        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


}