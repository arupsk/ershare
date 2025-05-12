package com.Andriod.ER.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.Andriod.ER.R;

/* loaded from: classes.dex */
public class Protaction_Activity extends AppCompatDialogFragment {
    private TextView P1;
    private TextView P10;
    private TextView P11;
    private TextView P12;
    private TextView P2;
    private TextView P3;
    private TextView P4;
    private TextView P5;
    private TextView P6;
    private TextView P7;
    private TextView P8;
    private TextView P9;
    public int[] Protaction_State;
    private TextView S1;
    private TextView S10;
    private TextView S11;
    private TextView S12;
    private TextView S2;
    private TextView S3;
    private TextView S4;
    private TextView S5;
    private TextView S6;
    private TextView S7;
    private TextView S8;
    private TextView S9;

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.activity_protaction_, (ViewGroup) null);
        this.Protaction_State = getArguments().getIntArray("protection_state");
        builder.setView(inflate).setTitle("Protection State").setNegativeButton(Html.fromHtml("<font color='#00000'>Cancel</font>"), new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.Protaction_Activity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        this.P1 = (TextView) inflate.findViewById(R.id.P1);
        this.P2 = (TextView) inflate.findViewById(R.id.P2);
        this.P3 = (TextView) inflate.findViewById(R.id.P3);
        this.P4 = (TextView) inflate.findViewById(R.id.P4);
        this.P5 = (TextView) inflate.findViewById(R.id.P5);
        this.P6 = (TextView) inflate.findViewById(R.id.P6);
        this.P7 = (TextView) inflate.findViewById(R.id.P7);
        this.P8 = (TextView) inflate.findViewById(R.id.P8);
        this.P9 = (TextView) inflate.findViewById(R.id.P9);
        this.P10 = (TextView) inflate.findViewById(R.id.P10);
        this.P11 = (TextView) inflate.findViewById(R.id.P11);
        this.P12 = (TextView) inflate.findViewById(R.id.P12);
        this.S1 = (TextView) inflate.findViewById(R.id.S1);
        this.S2 = (TextView) inflate.findViewById(R.id.S2);
        this.S3 = (TextView) inflate.findViewById(R.id.S3);
        this.S4 = (TextView) inflate.findViewById(R.id.S4);
        this.S5 = (TextView) inflate.findViewById(R.id.S5);
        this.S6 = (TextView) inflate.findViewById(R.id.S6);
        this.S7 = (TextView) inflate.findViewById(R.id.S7);
        this.S8 = (TextView) inflate.findViewById(R.id.S8);
        this.S9 = (TextView) inflate.findViewById(R.id.S9);
        this.S10 = (TextView) inflate.findViewById(R.id.S10);
        this.S11 = (TextView) inflate.findViewById(R.id.S11);
        this.S12 = (TextView) inflate.findViewById(R.id.S12);
        this.S1.setText("No");
        this.S2.setText("No");
        this.S3.setText("No");
        this.S4.setText("No");
        this.S5.setText("No");
        this.S6.setText("No");
        this.S7.setText("No");
        this.S8.setText("No");
        this.S9.setText("No");
        this.S10.setText("No");
        this.S11.setText("No");
        this.S12.setText("No");
        this.P1.setText("Cell Over Voltage:");
        this.P2.setText("Cell Under Voltage:");
        this.P3.setText("Pack Over Voltage:");
        this.P4.setText("Pack Under Voltage:");
        this.P5.setText("Charge Over Temperature:");
        this.P6.setText("Charge Under Temperature:");
        this.P7.setText("Discharge Over Temperature:");
        this.P8.setText("Discharge Under Temperature:");
        this.P9.setText("Charge Over Current:");
        this.P10.setText("Discharge Over Current:");
        this.P11.setText("Short circuit:");
        this.P12.setText("Internal Error:");
        if (this.Protaction_State[0] == 1) {
            this.S1.setText("Yes");
        }
        if (this.Protaction_State[1] == 1) {
            this.S2.setText("Yes");
        }
        if (this.Protaction_State[2] == 1) {
            this.S3.setText("Yes");
        }
        if (this.Protaction_State[3] == 1) {
            this.S4.setText("Yes");
        }
        if (this.Protaction_State[4] == 1) {
            this.S5.setText("Yes");
        }
        if (this.Protaction_State[5] == 1) {
            this.S6.setText("Yes");
        }
        if (this.Protaction_State[6] == 1) {
            this.S7.setText("Yes");
        }
        if (this.Protaction_State[7] == 1) {
            this.S8.setText("Yes");
        }
        if (this.Protaction_State[8] == 1) {
            this.S9.setText("Yes");
        }
        if (this.Protaction_State[9] == 1) {
            this.S10.setText("Yes");
        }
        if (this.Protaction_State[10] == 1) {
            this.S11.setText("Yes");
        }
        if (this.Protaction_State[11] == 1) {
            this.S12.setText("Yes");
        }
        return builder.create();
    }

    /* renamed from: com.Andriod.ER.com.Protaction_Activity$1 */
    class AnonymousClass1 implements DialogInterface.OnClickListener {
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
        }

        AnonymousClass1() {
        }
    }
}
