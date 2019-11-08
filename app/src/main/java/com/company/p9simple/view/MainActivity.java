package com.company.p9simple.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.company.p9simple.NavigationBackStackLogger;
import com.company.p9simple.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
