package com.calferinnovate.mediconnecta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.HomeView, new SesionFragment()).commit();
    }
}