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
public class CellenDialog extends AppCompatDialogFragment {
    private TextView cel1, cel2, cel3, cel4, cel5, cel6, cel7, cel8, cel9, cel10, cel11, cel12;
    private TextView cel13, cel14, cel15, cel16, cel17, cel18;
    private ProgressBar p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18;
    private FrameLayout f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18;
    public float[] cells;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_cells, null);
        // get arguments
        cells = getArguments().getFloatArray("cells");
        builder.setView(view)
                .setTitle("Battery Cells")
                .setNegativeButton(Html.fromHtml("<font color='#00000'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        cel1  = view.findViewById(R.id.cel1);
        cel2  = view.findViewById(R.id.cel2);
        cel3  = view.findViewById(R.id.cel3);
        cel4  = view.findViewById(R.id.cel4);
        cel5  = view.findViewById(R.id.cel5);
        cel6  = view.findViewById(R.id.cel6);
        cel7  = view.findViewById(R.id.cel7);
        cel8  = view.findViewById(R.id.cel8);
        cel9  = view.findViewById(R.id.cel9);
        cel10 = view.findViewById(R.id.cel10);
        cel11 = view.findViewById(R.id.cel11);
        cel12 = view.findViewById(R.id.cel12);
        cel13 = view.findViewById(R.id.cel13);
        cel14 = view.findViewById(R.id.cel14);
        cel15 = view.findViewById(R.id.cel15);
        cel16 = view.findViewById(R.id.cel16);
        cel17 = view.findViewById(R.id.cel17);
        cel18 = view.findViewById(R.id.cel18);

        p1 = view.findViewById(R.id.P_Cel1);
        p2 = view.findViewById(R.id.P_Cel2);
        p3 = view.findViewById(R.id.P_Cel3);
        p4 = view.findViewById(R.id.P_Cel4);
        p5 = view.findViewById(R.id.P_Cel5);
        p6 = view.findViewById(R.id.P_Cel6);
        p7 = view.findViewById(R.id.P_Cel7);
        p8 = view.findViewById(R.id.P_Cel8);
        p9 = view.findViewById(R.id.P_Cel9);
        p10 = view.findViewById(R.id.P_Cel10);
        p11 = view.findViewById(R.id.P_Cel11);
        p12 = view.findViewById(R.id.P_Cel12);
        p13 = view.findViewById(R.id.P_Cel13);
        p14 = view.findViewById(R.id.P_Cel14);
        p15 = view.findViewById(R.id.P_Cel15);
        p16 = view.findViewById(R.id.P_Cel16);
        p17 = view.findViewById(R.id.P_Cel17);
        p18 = view.findViewById(R.id.P_Cel18);

        f1 = view.findViewById(R.id.F1);
        f2 = view.findViewById(R.id.F2);
        f3 = view.findViewById(R.id.F3);
        f4 = view.findViewById(R.id.F4);
        f5 = view.findViewById(R.id.F5);
        f6 = view.findViewById(R.id.F6);
        f7 = view.findViewById(R.id.F7);
        f8 = view.findViewById(R.id.F8);
        f9 = view.findViewById(R.id.F9);
        f10 = view.findViewById(R.id.F10);
        f11 = view.findViewById(R.id.F11);
        f12 = view.findViewById(R.id.F12);
        f13 = view.findViewById(R.id.F13);
        f14 = view.findViewById(R.id.F14);
        f15 = view.findViewById(R.id.F15);
        f16 = view.findViewById(R.id.F16);
        f17 = view.findViewById(R.id.F17);
        f18 = view.findViewById(R.id.F18);


        cel1.setText("Cel1: " +  String.valueOf(new DecimalFormat("##.###").format(cells[1])) + "V");
        cel2.setText("Cel2: " +  String.valueOf(new DecimalFormat("##.###").format(cells[2])) + "V");
        cel3.setText("Cel3: " +  String.valueOf(new DecimalFormat("##.###").format(cells[3])) + "V");
        cel4.setText("Cel4: " +  String.valueOf(new DecimalFormat("##.###").format(cells[4])) + "V");
        cel5.setText("Cel5: " +  String.valueOf(new DecimalFormat("##.###").format(cells[5])) + "V");
        cel6.setText("Cel6: " +  String.valueOf(new DecimalFormat("##.###").format(cells[6])) + "V");
        cel7.setText("Cel7: " +  String.valueOf(new DecimalFormat("##.###").format(cells[7])) + "V");
        cel8.setText("Cel8: " +  String.valueOf(new DecimalFormat("##.###").format(cells[8])) + "V");
        cel9.setText("Cel9: " +  String.valueOf(new DecimalFormat("##.###").format(cells[9])) + "V");
        cel10.setText("Cel10: " +  String.valueOf(new DecimalFormat("##.###").format(cells[10])) + "V");
        cel11.setText("Cel11: " +  String.valueOf(new DecimalFormat("##.###").format(cells[11])) + "V");
        cel12.setText("Cel12: " +  String.valueOf(new DecimalFormat("##.###").format(cells[12])) + "V");
        cel13.setText("Cel13: " +  String.valueOf(new DecimalFormat("##.###").format(cells[13])) + "V");
        cel14.setText("Cel14: " +  String.valueOf(new DecimalFormat("##.###").format(cells[14])) + "V");
        cel15.setText("Cel15: " +  String.valueOf(new DecimalFormat("##.###").format(cells[15])) + "V");
        cel16.setText("Cel16: " +  String.valueOf(new DecimalFormat("##.###").format(cells[16])) + "V");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WorkCellenInterface();
        }
        f17.setVisibility(View.INVISIBLE);
        f18.setVisibility(View.INVISIBLE);
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void Work_per_cell(ProgressBar p, FrameLayout F, float data)
    {
        if(data != 0)
        {
            float Full = (float) 4.2;
            float Empty = (float) 2.6;
            float Range = Full - Empty;
            float Percent_cell = ((data - Empty) / Range) * 100;

            if((data>Empty) && (data<Full)) {
                p.setProgress((int) Percent_cell);
                if (Percent_cell < 30) {
                    p.setProgressTintList(ColorStateList.valueOf(Color.RED));

                } else if (Percent_cell > 30) {
                    p.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

                }
            }
            else if(data>Full)
            {
                p.setProgress(100);
                p.setProgressTintList(ColorStateList.valueOf(Color.RED));

            }

            else if(data<Empty)
            {
                p.setProgress(0);
                p.setProgressTintList(ColorStateList.valueOf(Color.RED));

            }

        }

        else
        {
            F.setVisibility(View.INVISIBLE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void WorkCellenInterface()
    {
        //ProgressBar p, FrameLayout F, float data
        Work_per_cell(p1, f1, cells[1]);
        Work_per_cell(p2, f2, cells[2]);
        Work_per_cell(p3, f3, cells[3]);
        Work_per_cell(p4, f4, cells[4]);
        Work_per_cell(p5, f5, cells[5]);
        Work_per_cell(p6, f6, cells[6]);
        Work_per_cell(p7, f7, cells[7]);
        Work_per_cell(p8, f8, cells[8]);
        Work_per_cell(p9, f9, cells[9]);
        Work_per_cell(p10, f10, cells[10]);
        Work_per_cell(p11, f11, cells[11]);
        Work_per_cell(p12, f12, cells[12]);
        Work_per_cell(p13, f13, cells[13]);
        Work_per_cell(p14, f14, cells[14]);
        Work_per_cell(p15, f15, cells[15]);
        Work_per_cell(p16, f16, cells[16]);
    }
}
