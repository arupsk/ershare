package com.Andriod.ER.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.Andriod.ER.R;

import java.text.DecimalFormat;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Protaction_Activity extends AppCompatDialogFragment {
    private TextView P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12;
    private TextView S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12;
    public int[] Protaction_State;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_protaction_, null);
        // get arguments
        Protaction_State = getArguments().getIntArray("protection_state");
        builder.setView(view)
                .setTitle("Protection State")
                .setNegativeButton(Html.fromHtml("<font color='#00000'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        P1  = view.findViewById(R.id.P1);
        P2  = view.findViewById(R.id.P2);
        P3  = view.findViewById(R.id.P3);
        P4  = view.findViewById(R.id.P4);
        P5  = view.findViewById(R.id.P5);
        P6  = view.findViewById(R.id.P6);
        P7  = view.findViewById(R.id.P7);
        P8  = view.findViewById(R.id.P8);
        P9  = view.findViewById(R.id.P9);
        P10  = view.findViewById(R.id.P10);
        P11  = view.findViewById(R.id.P11);
        P12  = view.findViewById(R.id.P12);


        S1  = view.findViewById(R.id.S1);
        S2  = view.findViewById(R.id.S2);
        S3  = view.findViewById(R.id.S3);
        S4  = view.findViewById(R.id.S4);
        S5  = view.findViewById(R.id.S5);
        S6  = view.findViewById(R.id.S6);
        S7  = view.findViewById(R.id.S7);
        S8  = view.findViewById(R.id.S8);
        S9  = view.findViewById(R.id.S9);
        S10  = view.findViewById(R.id.S10);
        S11  = view.findViewById(R.id.S11);
        S12  = view.findViewById(R.id.S12);

        S1.setText("No");
        S2.setText("No");
        S3.setText("No");
        S4.setText("No");
        S5.setText("No");
        S6.setText("No");
        S7.setText("No");
        S8.setText("No");
        S9.setText("No");
        S10.setText("No");
        S11.setText("No");
        S12.setText("No");

        P1.setText("Cell Over Voltage:");
        P2.setText("Cell Under Voltage:");
        P3.setText("Pack Over Voltage:");
        P4.setText("Pack Under Voltage:");
        P5.setText("Charge Over Temperature:");
        P6.setText("Charge Under Temperature:");
        P7.setText("Discharge Over Temperature:");
        P8.setText("Discharge Under Temperature:");
        P9.setText("Charge Over Current:");
        P10.setText("Discharge Over Current:");
        P11.setText("Short circuit:");
        P12.setText("Internal Error:");

        if(Protaction_State[0] == 1)
        {S1.setText("Yes");}
        if(Protaction_State[1] == 1)
        {S2.setText("Yes");}
        if(Protaction_State[2] == 1)
        {S3.setText("Yes");}
        if(Protaction_State[3] == 1)
        {S4.setText("Yes");}
        if(Protaction_State[4] == 1)
        {S5.setText("Yes");}
        if(Protaction_State[5] == 1)
        {S6.setText("Yes");}
        if(Protaction_State[6] == 1)
        {S7.setText("Yes");}
        if(Protaction_State[7] == 1)
        {S8.setText("Yes");}
        if(Protaction_State[8] == 1)
        {S9.setText("Yes");}
        if(Protaction_State[9] == 1)
        {S10.setText("Yes");}
        if(Protaction_State[10] == 1)
        {S11.setText("Yes");}
        if(Protaction_State[11] == 1)
        {S12.setText("Yes");}


        return builder.create();
    }




}
