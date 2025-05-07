package com.Andriod.ER.com;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.Andriod.ER.R;

import java.util.List;

import static java.lang.Math.abs;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    public int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";


    public boolean cellenDataReady = false;

    byte[] one = {};
    byte[] two = {};
    byte[] combined = {};

    // values decleration
    public float volts = (float) 00.0;
    public float amps = (float) 00.0;
    public float Ramps = (float) 00.0;
    public float percentage = (float) 00.0;
    public  float temp1 = (float) 00.0;
    public float temp2 = (float) 00.0;
    public float time = (float) 00.0;
    public float RemainCap = (float) 00.0;
    public float NomCap = (float) 00.0;
    public int AantalCellen = 0;
    public double[] cellen = {(float) 0.00};

    //protection data
    public int SOV = 0;
    public int SUV = 0;
    public int POV = 0;
    public int PUV = 0;
    public int COT = 0;
    public int CUT = 0;
    public int DOT = 0;
    public int DUT = 0;
    public int COC = 0;
    public int DOC = 0;
    public int SC = 0;
    public int IC_Error = 0;
    public float lockData = 0;


    public int amount_old_data = 0;
    public int amount_old_cellen_data = 0;


    //Calepration
    public int Temp_Sensors_Data = 0;
    public float NomCap_Par = (float) 00.0;
    public int Mosfets = 0;

    //Calibration parameters
    //                  Register                            Name                                                        //Unit
    public byte Reg_Design_capacity = 0x10;                 public float Design_capacity = (float) 00.0;                //10mAh
    public byte Reg_Circulation_capacity = 0x11;            public float Circulation_capacity = (float) 00.0;           //10mAh
    public byte Reg_Single_unit_full_voltage = 0x12;        public float Single_unit_full_voltage = (float) 00.0;       //mv
    public byte Reg_Single_cut_off_voltage = 0x12;          public float Single_cut_off_voltage = (float) 00.0;         //mv


    public byte Reg_Cell_Under_voltage_par = 0x26;          public float Cell_Under_voltage_par = (float) 00.0;                 //1mv
    public byte Reg_Cell_Under_voltage_releas_par = 0x27;   public float Cell_Under_voltage_releas_par = (float) 00.0;          //1mv
    public byte Reg_Pack_Under_voltage_par = 0x22;          public float Pack_Under_voltage_par = (float) 00.0;                 //10mv
    public byte Reg_Pack_Under_voltage_releas_par = 0x23;   public float Pack_Under_voltage_releas_par = (float) 00.0;          //10mv


    //cells
    float cel1 = 0;
    float cel2 = 0;
    float cel3 = 0;
    float cel4 = 0;
    float cel5 = 0;
    float cel6 = 0;
    float cel7 = 0;
    float cel8 = 0;
    float cel9 = 0;
    float cel10 = 0;
    float cel11 = 0;
    float cel12 = 0;
    float cel13 = 0;
    float cel14 = 0;
    float cel15 = 0;
    float cel16 = 0;

    public boolean ReadComand_ready = false;
    public boolean CellenCommand_ready = false;


    //Arrays for the old data
    public class oldData{
        //time data
        public float sec = (float) 00.0;
        public float min = (float) 00.0;
        public float hour = (float) 00.0;
        public float day = (float) 00.0;
        public float month = (float) 00.0;
        public float year = (float) 00.0;
        //basics data
        public float volts = (float) 00.0;
        public float amps = (float) 00.0;
        public float Ramps = (float) 00.0;
        public float percentage = (float) 00.0;
        public  float temp1 = (float) 00.0;
        public float temp2 = (float) 00.0;
        public float time = (float) 00.0;
        public float RemainCap = (float) 00.0;
        public float NomCap = (float) 00.0;
    }



    /*public float[] volts_old = {(float) 00.0};
    public float[] amps_old = {(float) 00.0};
    public float[] Ramps_old = {(float) 00.0};
    public float[] percentage_old = {(float) 00.0};
    public  float[] temp1_old = {(float) 00.0};
    public float[] temp2_old = {(float) 00.0};
    public float[] time_old = {(float) 00.0};
    public float[] RemainCap_old = {(float) 00.0};
    public float[] NomCap_old = {(float) 00.0};
    public static int[] AantalCellen_old = {(int) 0};
    public float[][] cellen_old = new float[][]{{(float) 0.00}, {(float) 0.00}};
*/



    public boolean DataReady = false;


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                                != PackageManager.PERMISSION_GRANTED) {

                    Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                    // Optional: notify the Activity to recheck permissions
                    return;
                }
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                Log.i(TAG, "uuid:" + gatt);

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                Log.i(TAG, "Charuuid:" + characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            Log.i(TAG, "CharChangeduuid:" + characteristic);

        }
    };


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);

    }

    //Read function
    private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        final byte[] data = characteristic.getValue();

        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            intent.putExtra(EXTRA_DATA,String.format("%s", new String(data)));
           //encodeHexString(data);
            ReadingData(data);
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }


    public void ReadingData(byte[] DataIn)
    {
        long begin = 0xdd;
        long end = 0x77;
        List list = null;
        if ((DataIn[0] & 0xFF) == begin)
        {
            one = DataIn;
           // Log.i(TAG, "New Data..");
            combined = one;
        }
        else
        {
            // merging two lines of data
            two = DataIn;
            //Log.i(TAG, "Adding data together..");
            byte[] c = new byte[one.length + two.length];
            System.arraycopy(one, 0, c, 0, one.length);
            System.arraycopy(two, 0, c, one.length, two.length);

            combined = c;
        }

        encodeHexString(combined);
      //  if ((combined[combined.length - 1] & 0xFF) == end)
       // {
            Log.i(TAG, "End of line.. Now DataProcessing");
            procesdata(combined);

       // }
    }


    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(String.format("%02X ", byteArray[i]));
        }
        Log.i(TAG,"Data hexString in:" + hexStringBuffer);
        return hexStringBuffer.toString();
    }

    public int getBit(int ID, int position)
    {
        return (ID >> position) & 1;
    }

    public void procesdata(byte[] DataIn)
    {
        if (((DataIn[1] & 0xFF) == 0x03)&&(DataIn.length>30))
        {
            Log.i(TAG, "DataIn[1] & 0xFF) == 0x03");

            float volts1 = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
            volts = volts1 / 100;

            float amps1 = ((DataIn[6] & 0xFF) << 8 | (DataIn[7] & 0xFF));
            amps = amps1;

            if (amps > 25000)
            {
                Ramps = (amps - 65536) / 100;
            }
            else if (amps < 25000)
            {
                Ramps = amps / 100;
            }
            float RemainCap1 = ((DataIn[8] & 0xFF) << 8 | (DataIn[9] & 0xFF));
            RemainCap = RemainCap1 / 100;

            float NomCap1 = ((DataIn[10] & 0xFF) << 8 | (DataIn[11] & 0xFF));
            NomCap = NomCap1 / 100;

            float percentage1 = DataIn[23];
            percentage = percentage1;


            //time
            if (Ramps != 00.00){
                if (Ramps < 00.00){ time = (RemainCap /  abs(Ramps));}
                else if (Ramps > 00.00) {time = ((NomCap - RemainCap) /  abs(Ramps));}
            }
            else if (amps == 00.00) { time = (float) 00.00;
            }

           float temp11 = ((DataIn[27] & 0xFF) << 8 | (DataIn[28] & 0xFF));
            temp1 = (temp11 - 2731) / 10;
            if ((DataIn[26] & 0xFF) == 0x02)
            {
                float temp22 = ((DataIn[29] & 0xFF) << 8 | (DataIn[30] & 0xFF));
                temp2 = (temp22 - 2731) / 10;

            }
            else if ((DataIn[26] & 0xFF) == 0x01)
            {
                temp2 = (float) 00.00;     //no temp2

            }

            int Protectionstate = ((DataIn[20] & 0xFF) << 8 | (DataIn[21] & 0xFF));
            lockData = Protectionstate >> 12;
            SOV = getBit(Protectionstate, 0);
            SUV = getBit(Protectionstate, 1);
            POV = getBit(Protectionstate, 2);
            PUV = getBit(Protectionstate, 3);
            COT = getBit(Protectionstate, 4);
            CUT = getBit(Protectionstate, 5);
            DOT = getBit(Protectionstate, 6);
            DUT = getBit(Protectionstate, 7);
            COC = getBit(Protectionstate, 7);
            DOC = getBit(Protectionstate, 9);
            SC = getBit(Protectionstate, 10);
            IC_Error = getBit(Protectionstate, 11);
            Mosfets = DataIn[24];
            //22 mosfet byte
            //24
           // Log.i(TAG, "Data prosessing finished!");

            DataReady = true;
           // Log.i(TAG, "lockData: " + lockData);
           /*   Log.i(TAG, "BattData: " + "/n V:" + volts + "/n I:" + Ramps + "/n percent:" + percentage + "/n Temps1:"
                  + temp1 + "/n Temps2:" +  temp2 + "/n time:" + time + "/n nomcap:" + NomCap  + "/n Remcap" + RemainCap);*/
            ReadComand_ready = true;
        }

        else if ((DataIn[1] & 0xFF) == 0x04) {
            AantalCellen = (DataIn[3] & 0xFF)/2;
            int y = 0;
            /*for(int x = 0; x < AantalCellen; x++) {

                //Log.i(TAG, "Ik ben hier, aanralcellen:"+ AantalCellen + " ,x=" + x);
                float cel = ((DataIn[4 + y] & 0xFF) << 8 | (DataIn[5 + y] & 0xFF));
                cellen[x] = cel/1000;
                y = y + 2;
               // Log.i(TAG, "cel " + x + "=" + cellen[x] + "V");
            }
          */
           // Log.i(TAG, "AantalCellen: "  + AantalCellen);
            process_Cells(DataIn, AantalCellen);
            cellenDataReady = true;

        }
//SD =>0xDD, 0x08, year, month, day, hour, minute, sec, (0x03 of oxo4), data

        else if ((DataIn[1] & 0xFF) == 0x2E)
        {
            Temp_Sensors_Data = (DataIn[5] & 0xFF);
        }

        else if ((DataIn[1] & 0xFF) == 0x10)
        {
            float NomCap1_par = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
            NomCap_Par = NomCap1_par / 100;
        }
        else if ((DataIn[1] & 0xFF) == Reg_Cell_Under_voltage_releas_par)
        {
            if(((DataIn[2] & 0xFF) != 0)||((DataIn[2] & 0xFF) != 80)) {
                float UVP_Par1 = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
                Cell_Under_voltage_releas_par = UVP_Par1 / 1000;
             //   Log.i(TAG, "Cell_Under_voltage_releas_par = " + Cell_Under_voltage_releas_par + " V");
            }

        }
        else if ((DataIn[1] & 0xFF) == Reg_Cell_Under_voltage_par)
        {
            if(((DataIn[2] & 0xFF) != 0)||((DataIn[2] & 0xFF) != 80)) {

                float UV_Par1 = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
                Cell_Under_voltage_par = UV_Par1 / 1000;
                //Log.i(TAG, "Cell_Under_voltage_par = " + Cell_Under_voltage_par + " V");
            }

        }
        else if ((DataIn[1] & 0xFF) == Reg_Pack_Under_voltage_releas_par)
        {
            if(((DataIn[2] & 0xFF) != 0)||((DataIn[2] & 0xFF) != 80)) {

                float UV_Par1 = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
                Pack_Under_voltage_releas_par = UV_Par1 / 100;
             //   Log.i(TAG, "Pack_Under_voltage_releas_par = " + Pack_Under_voltage_releas_par + " V");
            }

        }
        else if ((DataIn[1] & 0xFF) == Reg_Pack_Under_voltage_par)
        {
            if(((DataIn[2] & 0xFF) != 0)||((DataIn[2] & 0xFF) != 80)) {

                float UV_Par1 = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
                Pack_Under_voltage_par = UV_Par1 / 100;
              //  Log.i(TAG, "Pack_Under_voltage_par = " + Pack_Under_voltage_par + " V");
            }
        }

        else {
            Log.i(TAG, "Unexpected Data:" + DataIn);
            return;

        }

    }


    public void process_Cells(byte[] DataIn, int NumCells)
    {
        float cel = 0;
        //4,8,12 en 16
        if (NumCells >= 4)
        {
            cel = ((DataIn[4] & 0xFF) << 8 | (DataIn[5] & 0xFF));
            cel1 = cel/1000;
            cel = ((DataIn[6] & 0xFF) << 8 | (DataIn[7] & 0xFF));
            cel2 = cel/1000;
            cel = ((DataIn[8] & 0xFF) << 8 | (DataIn[9] & 0xFF));
            cel3 = cel/1000;
            cel = ((DataIn[10] & 0xFF) << 8 | (DataIn[11] & 0xFF));
            cel4 = cel/1000;
        }
        if (NumCells >= 8)
        {
            cel = ((DataIn[12] & 0xFF) << 8 | (DataIn[13] & 0xFF));
            cel5 = cel/1000;
            cel = ((DataIn[14] & 0xFF) << 8 | (DataIn[15] & 0xFF));
            cel6 = cel/1000;
            cel = ((DataIn[16] & 0xFF) << 8 | (DataIn[17] & 0xFF));
            cel7 = cel/1000;
            cel = ((DataIn[18] & 0xFF) << 8 | (DataIn[19] & 0xFF));
            cel8 = cel/1000;
        }
        if (NumCells >= 12)
        {
            cel = ((DataIn[20] & 0xFF) << 8 | (DataIn[21] & 0xFF));
            cel9 = cel/1000;
            cel = ((DataIn[22] & 0xFF) << 8 | (DataIn[23] & 0xFF));
            cel10 = cel/1000;
            cel = ((DataIn[24] & 0xFF) << 8 | (DataIn[25] & 0xFF));
            cel11 = cel/1000;
            cel = ((DataIn[26] & 0xFF) << 8 | (DataIn[27] & 0xFF));
            cel12 = cel/1000;
        }
        if (NumCells >= 16)
        {
            cel = ((DataIn[28] & 0xFF) << 8 | (DataIn[29] & 0xFF));
            cel13 = cel/1000;
            cel = ((DataIn[30] & 0xFF) << 8 | (DataIn[31] & 0xFF));
            cel14 = cel/1000;
            cel = ((DataIn[32] & 0xFF) << 8 | (DataIn[33] & 0xFF));
            cel15 = cel/1000;
            cel = ((DataIn[34] & 0xFF) << 8 | (DataIn[35] & 0xFF));
            cel16 = cel/1000;
        }

        CellenCommand_ready = true;
       /* Log.i(TAG, "cel " + 1 + "=" + cel1 + "V" + ",cel " + 2 + "=" + cel2+ ",cel " + 3 + "=" + cel3 + ",cel " + 4 + "=" + cel4
                + "cel " + 5 + "=" + cel5 + "V" + ",cel " + 6 + "=" + cel6+ ",cel " + 7 + "=" + cel7 + ",cel " + 8 + "=" + cel8
                + "cel " + 9 + "=" + cel9 + "V" + ",cel " + 10 + "=" + cel10+ ",cel " + 11 + "=" + cel11 + ",cel " + 12 + "=" + cel12
                + "cel " + 13 + "=" + cel13 + "V" + ",cel " + 14 + "=" + cel14+ ",cel " + 15 + "=" + cel15 + ",cel " + 16 + "=" + cel16);*/
        //Log.i(TAG, "Percentage: "+ percentage + "%");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null) {
            Log.w(TAG, "mBluetoothAdapter is null.");
        }
        if (address == null) {
            Log.w(TAG, "Device address is null.");
        }
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
                // Optional: notify the Activity to recheck permissions
                return false;
            }
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
                mBluetoothDeviceAddress = address;
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
        Log.i(TAG, "characteristic" + characteristic);
    }

    /**
     * Write to a given char
     * @param characteristic The characteristic to write to
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
}
