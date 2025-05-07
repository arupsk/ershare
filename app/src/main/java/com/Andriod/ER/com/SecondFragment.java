package com.Andriod.ER.com;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.Andriod.ER.R;

import java.util.concurrent.TimeUnit;


public class SecondFragment extends android.app.Fragment {

    View view;
    Button secondButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }

    public void LockCalFunc(View view) throws InterruptedException {

    }

    public void Temp1(View view) throws InterruptedException {

    }

    public void Temp2(View view) throws InterruptedException {

    }

    public void Temp3(View view) throws InterruptedException {

    }

}
