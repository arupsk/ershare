package com.Andriod.ER.com;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.Andriod.ER.R;

import java.text.DecimalFormat;

public class FirstFragment extends android.app.Fragment {

    public int ReaAmps = 0;
    public TextView Realcurrent;
    private Calibration mCalibration;
    private BluetoothLeService mBluetoothLeService;

    View view;
    Button RefButton;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        //ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        Calibration xxx = (Calibration) getActivity();

        Realcurrent = (TextView) view.findViewById(R.id.realamps);

        // get the reference of Button
        RefButton = (Button) view.findViewById(R.id.Refresh_First);
// perform setOnClickListener on first Button
        RefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// display a message by using a Toast
                Toast.makeText(getActivity(), "Refreshing.. ", Toast.LENGTH_LONG).show();
               // Realcurrent.setText(String.valueOf(new DecimalFormat("##.#").format(Calibration.r) + "A"));
                //Bundle bundle = getArguments();
                //String message = bundle.getString("message");
              //  Realcurrent.setText(StringVaReaAmps);
                ReaAmps = (int) mBluetoothLeService.Ramps;

                Realcurrent.setText(String.valueOf(new DecimalFormat("##.#").format(ReaAmps) + "A"));

            }
        });

        return view;
    }


    public void Cal_STamps(View view) throws InterruptedException {

        Toast.makeText(getActivity(), "Calebrating", Toast.LENGTH_LONG).show();

    }

    public void Cal_Disamps(View view) throws InterruptedException {

        Toast.makeText(getActivity(), "Calebrating", Toast.LENGTH_LONG).show();

    }

    public void Cal_CHamps(View view) throws InterruptedException {

        Toast.makeText(getActivity(), "Calebrating", Toast.LENGTH_LONG).show();

    }

}
