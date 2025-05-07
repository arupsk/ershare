package com.Andriod.ER.com;

import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Transmission {
    private final static String TAG = Transmission.class.getSimpleName();

    private static BluetoothLeService mBluetoothLeService;


    public static byte ReadCommand[]  = {(byte) 221, (byte) 165, (byte) 3, (byte) 0, (byte) 255, (byte) 253, (byte) 119, (byte) 13, (byte) 10};
    public static  byte LockCommand[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0xE1, (byte) 0x2, (byte) 0x0, (byte) 0x2, (byte) 0xFF, (byte) 0x1B, (byte) 0x77,};
    public static byte UnlockCommand[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0xE1, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0x1D, (byte) 0x77,};

    public static  byte CellenReadCommand[]  = {(byte) 221, (byte) 165, (byte) 4, (byte) 0, (byte) 255, (byte) 252, (byte) 119, (byte) 13, (byte) 10};
    public static byte HistoryReadCommand[]  = {(byte) 0xdd, (byte) 0xaa,  (byte) 0x77};

    public static byte fabriekMode[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x0, (byte) 0x2, (byte) 0x56, (byte) 0x78, (byte) 0xFF, (byte) 0x30, (byte) 0x77,};

    public static byte OUTfabriekMode_read[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x01, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0xFD, (byte) 0x77,};

    public static byte OUTfabriekMode[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x01, (byte) 0x2, (byte) 0x28, (byte) 0x28, (byte) 0xFF, (byte) 0xAD, (byte) 0x77,};


    public static int Pas_Zero[] = {1, 2, 3, 4, 5, 6};



    public static byte[] Match_Password(int[] Pass1){


        int register_int = 0x06;
        int checksum = Pass1[0] + Pass1[1] + Pass1[2] + Pass1[3] + Pass1[4] + Pass1[5] + register_int + 7 + 6 - 1; //(Data + 0xRegister + Data Length - 1) ^ 0xFFFF
        //Log.i(TAG, "checksum = " + Data0_int + "+" + Data1_int + "+" + register_int + "+" +  1 + "=" + (Data0_int + Data1_int + register_int + 1) + "=" + (checksum));
        byte CHECK_L = (byte) (checksum & 0xFF);
        byte CHECK_H = (byte) ((checksum >> 8) & 0xFF);

        byte Match_Password[]  = {(byte) 0xdd, (byte) 0x5a, (byte) 0x06, (byte) 7, (byte) 6,
                (byte) Pass1[0], (byte) Pass1[1], (byte) Pass1[2], (byte) Pass1[3], (byte) Pass1[4], (byte) Pass1[5],
                (byte) (CHECK_H^0xFF), (byte) (CHECK_L^0xFF), (byte) 0x77};


        return Match_Password;
    }


    public static byte[] Change_Password(int[] PassOld, int[] PassNew ){


        int register_int = 0x07;
        int checksum = PassOld[0] + PassOld[1] + PassOld[2] + PassOld[3] + PassOld[4] + PassOld[5]
                + PassNew[0] + PassNew[1] + PassNew[2] + PassNew[3] + PassNew[4] + PassNew[5]
                + register_int + 13 + 12 - 1; //(Data + 0xRegister + Data Length - 1) ^ 0xFFFF
        //Log.i(TAG, "checksum = " + Data0_int + "+" + Data1_int + "+" + register_int + "+" +  1 + "=" + (Data0_int + Data1_int + register_int + 1) + "=" + (checksum));
        byte CHECK_L = (byte) (checksum & 0xFF);
        byte CHECK_H = (byte) ((checksum >> 8) & 0xFF);

        byte Change_Password[]  = {(byte) 0xdd, (byte) 0x5a, (byte) 0x07, (byte) 13, (byte) 12,
                (byte) PassOld[0], (byte) PassOld[1], (byte) PassOld[2], (byte) PassOld[3], (byte) PassOld[4], (byte) PassOld[5],
                (byte) PassNew[0], (byte) PassNew[1], (byte) PassNew[2], (byte) PassNew[3], (byte) PassNew[4], (byte) PassNew[5],
                (byte) (CHECK_H^0xFF), (byte) (CHECK_L^0xFF), (byte) 0x77};


        return Change_Password;
    }



}
