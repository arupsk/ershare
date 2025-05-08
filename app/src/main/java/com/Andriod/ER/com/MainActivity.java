package com.Andriod.ER.com;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Andriod.ER.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static android.app.AlertDialog.*;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private Transmission Tx;

    public boolean busy = false;
    public  byte ReadCommand[]  = {(byte) 221, (byte) 165, (byte) 3, (byte) 0, (byte) 255, (byte) 253, (byte) 119, (byte) 13, (byte) 10};
    public  byte LockCommand[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0xE1, (byte) 0x2, (byte) 0x0, (byte) 0x2, (byte) 0xFF, (byte) 0x1B, (byte) 0x77,};
    public  byte UnlockCommand[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0xE1, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0x1D, (byte) 0x77,};

    public  byte CellenReadCommand[]  = {(byte) 221, (byte) 165, (byte) 4, (byte) 0, (byte) 255, (byte) 252, (byte) 119, (byte) 13, (byte) 10};
    public  byte HistoryReadCommand[]  = {(byte) 0xdd, (byte) 0xaa,  (byte) 0x77};

    public  byte fabriekMode[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x0, (byte) 0x2, (byte) 0x56, (byte) 0x78, (byte) 0xFF, (byte) 0x30, (byte) 0x77,};

    public  byte OUTfabriekMode_read[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x01, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0xFD, (byte) 0x77,};

    public  byte OUTfabriekMode[]  = {(byte) 0xDD, (byte) 0x5A, (byte) 0x01, (byte) 0x2, (byte) 0x28, (byte) 0x28, (byte) 0xFF, (byte) 0xAD, (byte) 0x77,};


    public int Pas_Zero[] = {1, 2, 3, 4, 5, 6};

    public boolean packegeReady = false;
    public boolean cellenready = false;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private static final int REQUEST_BLUETOOTH_CONNECT = 1003;  // This can be any integer value

    private String mDeviceName;
    private String mDeviceAddress;
    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private HistoryActivity History;
    public static boolean mConnected = false;
    public static boolean FinalMconected = false;
    public static boolean FinalMconected2 = false;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;


    public static boolean getConnectie() {
        return mConnected;
    }

    public int command = 0;
    private boolean ReadyToUnkink = false;
    public boolean Boolforground = true;
    public BluetoothGattCharacteristic characteristicTX;
    public BluetoothGattCharacteristic characteristicRX;
    public UUID myisscRxUUID = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    public UUID myisscTxUUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    public UUID myisscServiceUUID = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");
    public TextView percent;
    public TextView voltage;
    public TextView current;
    public TextView time;
    public TextView temp;
    public TextView BatteryNum;
    private ProgressBar mProgressBar;
    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "0xffffffff" ;
    public ImageButton lockButton;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private static final int MY_PERMISSIONS_REQUEST_CODE_Scan_Connect = 124;

    public TextView V_cell, V_pack, OC, OT, Error;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Refresh(View view) throws InterruptedException {
        // Do something in response to button
        // Get the Intent that started this activity and extract the string

        //*     finding the interface Textview s    */

        // encodeHexString(Match_Password(getRandomNumberNumber()));
        encodeHexString(Tx.Match_Password(Tx.Pas_Zero));
        encodeHexString(Tx.Change_Password(Tx.Pas_Zero, new int[]{1, 2, 3, 4, 5, 6}));


        if (mConnected == true) {

            // TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(ReadCommand);
            TimeUnit.MILLISECONDS.sleep(500);
            sendDataToBLE(CellenReadCommand);
            TimeUnit.MILLISECONDS.sleep(200);
            WorkInterface();

            //Calibration Test:
           /*busy = true;
            sendDataToBLE(fabriekMode);
            TimeUnit.MILLISECONDS.sleep(500);
            Calibrate((int)(Call_UV_save*1000), (byte) 0x26);   //UNDERVOLTAGE 2.65V
            TimeUnit.MILLISECONDS.sleep(500);
            sendDataToBLE(OUTfabriekMode);
            busy = false;*/
            // Calibrate_Safe_Mode();



        } else {
            if (mBluetoothLeService.mConnectionState == STATE_DISCONNECTED) {
                Openscan();
            }
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            BatteryNum.setText("ER34765019126");
            percent.setText("64%");
            voltage.setText("13.2V");
            current.setText("0A");
            time.setText("0h");
            temp.setText("19°C");
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            mProgressBar.setProgress((int) 64);
        }

        //simulation mode
      /* BatteryNum.setText("ER2019546520");
        percent.setText("55.5%");
        voltage.setText("33.5V");
        current.setText("-50.0A");
        time.setText("1.2h");
        temp.setText("20.9°C");
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        mProgressBar.setProgress((int) 55.5);*/
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Lock(View view) throws InterruptedException {
        if (mConnected == true) {

            if(mBluetoothLeService.lockData == 0) {
                TimeUnit.SECONDS.sleep(1);
                sendDataToBLE(LockCommand);
                TimeUnit.SECONDS.sleep(1);
            }
            else
            {
                TimeUnit.SECONDS.sleep(1);
                sendDataToBLE(UnlockCommand);
                TimeUnit.SECONDS.sleep(1);
            }

            WorkInterface();
        }
    }

    private void disconnect_BLE()
    {

        if (mBluetoothAdapter == null || mBluetoothLeService == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothLeService.disconnect();
        mBluetoothLeService.close();
        mConnected = false;
        FinalMconected = false;
        characteristicTX = null;

        ClearInterface();



    }

    //    public void Refresh(View view) throws InterruptedException {
    public void log_out(View view)
    {
        Intent i = new Intent(MainActivity.this, LogIn.class);
        startActivity(i);

    }
    public void Inform_Warning(View view) {
        if(mBluetoothLeService.SOV == 1)
        {
            Toast.makeText(this, "Single overvoltage protection.. Stop charging the battery!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.SUV == 1)
        {
            Toast.makeText(this, "Single undervoltage protection.. Pleas charge the battery!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.POV == 1)
        {
            Toast.makeText(this, "Pack overvoltage protection.. Stop charging the battery!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.PUV == 1)
        {
            Toast.makeText(this, "Pack undervoltage protection.. Pleas charge the battery!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.COT == 1)
        {
            Toast.makeText(this, "(Charge) over temperature protection! .. Battery to HOT!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.CUT == 1)
        {
            Toast.makeText(this, "(Charge) under temperature protection! .. Battery to Cold!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.DOT == 1)
        {
            Toast.makeText(this, "(discharge) over temperature protection! .. Battery to HOT!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.DUT == 1)
        {
            Toast.makeText(this, "(discharge) under temperature protection! .. Battery to Cold!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.COC == 1)
        {
            Toast.makeText(this, "(charge)  over current protection!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.DOC == 1)
        {
            Toast.makeText(this, "(discharge)  over current protection!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.SC == 1)
        {
            Toast.makeText(this, "Short circuit protection (check your wire)!", Toast.LENGTH_LONG).show();
        }
        if(mBluetoothLeService.IC_Error == 1)
        {
            Toast.makeText(this, "Internal Error!", Toast.LENGTH_LONG).show();
        }


    }
    public void Link(View view) {


        if (mConnected == true)
        {

            disconnect_BLE();

            if(mBluetoothLeService.mConnectionState == STATE_DISCONNECTED ) {
                Openscan();
            }
        }
        else {
            Openscan();
        }
    }

    public void UnLink(View view) {
        if (mConnected == true)
        {
            disconnect_BLE();
        }
        ClearInterface();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void History(View view) throws InterruptedException {
        if (mConnected == true)
        {

            TimeUnit.SECONDS.sleep(1);
            sendDataToBLE(HistoryReadCommand);
            TimeUnit.SECONDS.sleep(1);
        }

        final Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);


        // else{
        //     Openscan();
        // }


    }

    public void Openscan(){
        final Intent intent = new Intent(this, DeviceScanActivity.class);
        startActivity(intent);
    }

    public void startGround(){
        Log.i(TAG, "Functie startGround..");

        final Intent intent = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this, intent);
        // Should we show an explanation?
        // Here, thisActivity is the current activity
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void WorkInterface(){
        /*TextView percent = findViewById(R.id.Percentage);
        TextView voltage = findViewById(R.id.Voltage);
        TextView current = findViewById(R.id.Current);
        TextView time = findViewById(R.id.Time);
        TextView temp = findViewById(R.id.Temperature);
        TextView BatteryNum = findViewById(R.id.BatteryNo);
*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            BatteryNum.setText("ER34765019126");
            percent.setText("64%");
            voltage.setText("13.2V");
            current.setText("0A");
            time.setText("0h");
            temp.setText("19°C");
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            mProgressBar.setProgress((int) 64);
        }
        else {
            BatteryNum.setText(mDeviceName);
            percent.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.percentage) + "%"));
            voltage.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.volts) + "V"));
            current.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.Ramps) + "A"));
            time.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.time) + "h"));
            temp.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.temp1) + "°C"));
            mProgressBar.setProgress((int) mBluetoothLeService.percentage);
        }
        if (mBluetoothLeService.percentage < 40)
        {
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        }
        else if (mBluetoothLeService.percentage > 40)
        {
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

        }

        if(mBluetoothLeService.lockData == 0) {
            lockButton.setImageResource(R.drawable.unlock_10);
        }
        else if((mBluetoothLeService.lockData == 1)) {
            lockButton.setImageResource(R.drawable.lock_11);
        }

        /*if(mBluetoothLeService.SOV == 1)
        {
            V_cell.setVisibility(View.VISIBLE);
            V_cell.setText("SOV");
        }else{V_cell.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.SUV == 1)
        {
            V_cell.setVisibility(View.VISIBLE);
            V_cell.setText("SUV");
        }else{V_cell.setVisibility(View.INVISIBLE); }*/
        if(mBluetoothLeService.POV == 1)
        {
            V_pack.setVisibility(View.VISIBLE);
            V_pack.setText("POV");
        }else{V_pack.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.PUV == 1)
        {
            V_pack.setVisibility(View.VISIBLE);
            V_pack.setText("PUV");
        }else{V_pack.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.COT == 1)
        {
            OT.setVisibility(View.VISIBLE);
            OT.setText("COT");
        }else{OT.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.CUT == 1)
        {
            OT.setVisibility(View.VISIBLE);
            OT.setText("CUT");
        }else{OT.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.DOT == 1)
        {
            OT.setVisibility(View.VISIBLE);
            OT.setText("DOT");
        }else{OT.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.DUT == 1)
        {
            OT.setVisibility(View.VISIBLE);
            OT.setText("DUT");
        }else{OT.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.COC == 1)
        {
            OC.setVisibility(View.VISIBLE);
            OC.setText("COC");
        }else{OC.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.DOC == 1)
        {
            OC.setVisibility(View.VISIBLE);
            OC.setText("DOC");
        }else{OC.setVisibility(View.INVISIBLE); }
        if(mBluetoothLeService.SC == 1)
        {
            Error.setVisibility(View.VISIBLE);
            Error.setText("SC");
        }else{Error.setVisibility(View.INVISIBLE); }

        if(mBluetoothLeService.IC_Error == 1)
        {
            Error.setVisibility(View.VISIBLE);
            Error.setText("IC_Error");
        }else{Error.setVisibility(View.INVISIBLE); }





        mBluetoothLeService.DataReady = false;
    }


    public void ClearInterface(){
        /*TextView percent = findViewById(R.id.Percentage);
        TextView voltage = findViewById(R.id.Voltage);
        TextView current = findViewById(R.id.Current);
        TextView time = findViewById(R.id.Time);
        TextView temp = findViewById(R.id.Temperature);
        TextView BatteryNum = findViewById(R.id.BatteryNo);*/

        BatteryNum.setText("Energy-Research");
        percent.setText("00.0%");
        voltage.setText("00.0V");
        current.setText("00.0A");
        time.setText("00.0h");
        temp.setText("00.0°C");
        mProgressBar.setProgress((int) 0);
      //  V_cell.setVisibility(View.INVISIBLE);
        V_pack.setVisibility(View.INVISIBLE);
        OT.setVisibility(View.INVISIBLE);
        OC.setVisibility(View.INVISIBLE);
        Error.setVisibility(View.INVISIBLE);


    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Log.i(TAG, "mConnected = true; ");

                //updateConnectionState(R.string.connected);
                // invalidateOptionsMenu();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                FinalMconected2 = true;
                Log.i(TAG, "FinalMconected2 = true; ");

            }

            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                FinalMconected = false;

                FinalMconected2 = false;

                Log.i(TAG, "mConnected = false; ");

                //updateConnectionState(R.string.disconnected);
                //invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                if (mConnected) {
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());
                }
            }
        }
    };




    protected void checkPermission11(){
        Log.i(TAG,"checkPermission11");

        if(     ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(     ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                Builder builder = new Builder(this);
                builder.setMessage("Access to Bluetooth and Location" +
                        " are required to do the task.");
                builder.setTitle("Please grant those permissions");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= 31) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{
                                            Manifest.permission.BLUETOOTH_SCAN,
                                            Manifest.permission.BLUETOOTH_CONNECT,
                                    },
                                    MY_PERMISSIONS_REQUEST_CODE_Scan_Connect
                            );
                            return;
                        }

                        ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{
                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    },
                                    MY_PERMISSIONS_REQUEST_CODE
                            );
                        }
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                if (Build.VERSION.SDK_INT >= 31) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.BLUETOOTH_CONNECT,
                            },
                            MY_PERMISSIONS_REQUEST_CODE_Scan_Connect
                    );
                }

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            },
                            MY_PERMISSIONS_REQUEST_CODE
                    );
                }


            }
        }else {
            // Do something, when permissions are already granted
            // Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }


    protected void checkPermission12(){
        Log.i(TAG,"checkPermission12");

        if(     ContextCompat.checkSelfPermission(
                this,Manifest.permission.BLUETOOTH_SCAN)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.BLUETOOTH_CONNECT)
               /* + ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION)*/
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.BLUETOOTH_SCAN)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.BLUETOOTH_CONNECT)
                   /* || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_COARSE_LOCATION)*/  ){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                Builder builder = new Builder(this);
                builder.setMessage("Access to Bluetooth Scan and Bluetooth Connect" +
                        " are required to do the task.");
                builder.setTitle("Please grant those permissions");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= 31) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{
                                            Manifest.permission.BLUETOOTH_SCAN,
                                            Manifest.permission.BLUETOOTH_CONNECT,
                                    },
                                    MY_PERMISSIONS_REQUEST_CODE_Scan_Connect
                            );
                        }

                      /*  ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );*/

                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                if (Build.VERSION.SDK_INT >= 31) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.BLUETOOTH_CONNECT,
                            },
                            MY_PERMISSIONS_REQUEST_CODE_Scan_Connect
                    );
                }

               /* ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );*/

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            },
                            MY_PERMISSIONS_REQUEST_CODE
                    );
                }*/


            }
        }else {
            // Do something, when permissions are already granted
            //  Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }
    protected void checkPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            checkPermission12();
        }
        else  {checkPermission11();}

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mConnected = false;
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // 🔐 Add permission checks here BEFORE anything starts the ForegroundService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        },
                        1001);
                return;
            }
        }

        /*Intent mainIntent = new Intent(MainActivity.this, SplashActivity.class);
        MainActivity.this.startActivity(mainIntent);*/

        setContentView(R.layout.activity_main);
        // Make app draw edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewGroup rootView = findViewById(R.id.Main);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

            // Add padding to avoid overlapping status bar and navigation bar
            v.setPadding(0, topInset, 0, bottomInset);
            return insets;
        });
        getSupportActionBar().hide();

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        BatteryNum = findViewById(R.id.BatteryNo);
        BatteryNum.setText("Energy-Research");

        percent = findViewById(R.id.Percentage);
        voltage = findViewById(R.id.Voltage);
        current = findViewById(R.id.Current);
        time = findViewById(R.id.Time);
        temp = findViewById(R.id.Temperature);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        lockButton = findViewById(R.id.Lockbut);

        //public TextView V_cell, V_pack, OC, OT, Error;
       // V_cell = findViewById(R.id.Single);
        V_pack = findViewById(R.id.Pack);
        OT = findViewById(R.id.TempProtect);
        OC = findViewById(R.id.CurrentProt);
        Error = findViewById(R.id.Error);

        //V_cell.setVisibility(View.INVISIBLE);
        V_pack.setVisibility(View.INVISIBLE);
        OT.setVisibility(View.INVISIBLE);
        OC.setVisibility(View.INVISIBLE);
        Error.setVisibility(View.INVISIBLE);


        percent.bringToFront();
        voltage.bringToFront();
        current.bringToFront();
        time.bringToFront();
        temp.bringToFront();
        ClearInterface();
        startGround();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        startThread();

        //Permissions for Bluetooth
       // checkPermission();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            //Intent i =new Intent(DeviceScanActivity.this,MainActivity.class);
            // startActivity(i);
            return;
        }
       /* mBtnDoTask = findViewById(R.id.link);
        checkPermission();
        // Set a click listener for the button
        mBtnDoTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    checkPermission();
                }
            }
        });*/


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions are granted
                    Toast.makeText(this, "Location Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
                    //  checkPermission();
                    // Permissions are denied
                    Toast.makeText(this, "Location Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CODE_Scan_Connect: {
                // When request is cancelled, the results array are empty
                if (
                        (grantResults.length > 0) &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions are granted
                    Toast.makeText(this, "Scan_Connect Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
                    //  checkPermission();
                    // Permissions are denied
                    Toast.makeText(this, "Scan_Connect Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    public void startThread()
    {
        MyThread thread = new MyThread();
        thread.start();
    }
    public void stopThread()
    {
        MyThread thread = new MyThread();
        thread.stop();
    }


    public class MyThread extends Thread {


        MyThread()
        {

        }
        @Override
        public void run(){
            Log.i(TAG,"MyThread running");
            while (true)
            {
                if(!busy){
                    if ((!mConnected)){
                    } else if ((mConnected)&&(FinalMconected)&&(characteristicTX != null)) {

                        sendDataToBLE(ReadCommand);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendDataToBLE(CellenReadCommand);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateInterFace();
                        packegeReady = true;
                        command = 1;

                    }
                }

            }
        }
    }



    void sendDataToBLE(byte str[]) {

        if (characteristicTX != null) {
            final byte[] data = str;
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                encodeHexString(data);

            }
            if ((mConnected) && (FinalMconected)) {
                characteristicTX.setValue(str);
                mBluetoothLeService.writeCharacteristic(characteristicTX);
                mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
            }
        }
    }


    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(String.format("%02X ", byteArray[i]));
        }
        Log.i(TAG,"Data hexString out:" + hexStringBuffer.toString());
        return hexStringBuffer.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Back to MainActivity..");

        // Step 1: Register GATT update receiver
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter(), RECEIVER_NOT_EXPORTED);

        // Step 2: Check Bluetooth permission (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission. Requesting...");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_CONNECT);
            return; // Don't try to connect until permission granted
        }

        // Step 3: Attempt reconnection only if device address is valid
        if (mBluetoothLeService != null && mDeviceAddress != null) {
            Log.i(TAG, "Attempting BLE reconnect to: " + mDeviceAddress);
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.i(TAG, "Connect request result = " + result);
        } else {
            Log.w(TAG, "BluetoothLeService or device address is null. Skipping connection.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mGattUpdateReceiver);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Receiver already unregistered.");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1000142:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case 100083:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateInterFace() {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if ((mConnected)&&(FinalMconected)) {
                    if (mBluetoothLeService.DataReady) {
                        WorkInterface();
                    }
                }
            }
        });
    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        UUID uuid = null;
        String UUIDCharacters = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        BluetoothGattCharacteristic characteristic = null;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid();
            Log.i(TAG, "UUIDService:" + String.valueOf(uuid));
            // If the service is the one say so.
            if (uuid.equals(myisscServiceUUID)) {
                Log.i(TAG, "Service Descoverd");
                // get characteristic when UUID matches RX/TX UUID
                characteristicTX = gattService.getCharacteristic(myisscTxUUID);
                Log.i(TAG, "UUIDTX:" + String.valueOf(characteristicTX));
                characteristicRX = gattService.getCharacteristic(myisscRxUUID);
                Log.i(TAG, "UUIDRX:" + String.valueOf(characteristicRX));

            }


        }
        FinalMconected = true;
        Log.i(TAG, "FinalMconected = true;");


    }


    public void open_Dialog_Cells(View view) throws InterruptedException {
        if(mConnected) {
            //send data setting
            Bundle bundle = new Bundle();
            float[] cells = {(float) mBluetoothLeService.AantalCellen, mBluetoothLeService.cel1, mBluetoothLeService.cel2, mBluetoothLeService.cel3, mBluetoothLeService.cel4,
                    mBluetoothLeService.cel5, mBluetoothLeService.cel6, mBluetoothLeService.cel7, mBluetoothLeService.cel8,
                    mBluetoothLeService.cel9, mBluetoothLeService.cel10, mBluetoothLeService.cel11, mBluetoothLeService.cel12,
                    mBluetoothLeService.cel13, mBluetoothLeService.cel14, mBluetoothLeService.cel15, mBluetoothLeService.cel16};
            bundle.putFloatArray("cells", cells);

            CellenDialog cellenDialog = new CellenDialog();

            //send data
            cellenDialog.setArguments(bundle);

            cellenDialog.show(getSupportFragmentManager(), "Battery Cells");
        }
    }



    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public static int[] getRandomNumberNumber() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int intArray[];    //declaring array
        intArray = new int[6];  // allocating memory to array
        intArray[0] = rnd.nextInt(9);
        intArray[1] = rnd.nextInt(9);
        intArray[2] = rnd.nextInt(9);
        intArray[3] = rnd.nextInt(9);
        intArray[4] = rnd.nextInt(9);
        intArray[5] = rnd.nextInt(9);

        // this will convert any number sequence into 6 character.
        return intArray;
    }

    public void Reserve(View view) throws InterruptedException
    {
        if (mConnected) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("If you use your reserve capacity, you are obliged to charge your battery within 24 hours. If you do not charge your battery, you will lose your guarantee." +
                    "  \n When you press ok, data is collected into our database to prove that you have indeed charged your battery.");
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //put your code that needed to be executed when okay is clicked
                            try {
                                Calibrate_UV((float) 2.65, (float) 3);//normal
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dialog.cancel();


                        }
                    });
            builder1.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }

    public void Registration(View view) throws InterruptedException {
        busy = true;
        Calibrate_UV((float) 2.85, (float) 3.1);//hoger
        busy = false;
    }

    public void Calibrate_UV(float UV, float UV_release) throws InterruptedException {
        if (mConnected == true)
        {
            busy = true;

            int cells = mBluetoothLeService.AantalCellen;
            float UVP = (float) (cells * UV * 100);
            float UVPR = (float) (cells * UV_release * 100);
            Log.i(TAG, "cells: " + cells + " ,UVP = " + UVP + " ,UVPR= " + UVPR );

            // mProgressDialog = ProgressDialog.show(Calibration.this,"Calibrating!", "Please wait...",false,false);
            sendDataToBLE(fabriekMode);
            TimeUnit.MILLISECONDS.sleep(500);
            Calibrate((int)(UV*1000), (byte) 0x26);   //UNDERVOLTAGE 2.65V
            TimeUnit.MILLISECONDS.sleep(500);
            Calibrate((int)(UV_release*1000), (byte) 0x27);   //UNDERVOLTAGE RELEASE 3V
            TimeUnit.MILLISECONDS.sleep(500);
            Calibrate((int)(UVP), (byte) 0x22);   //PACK UNDERVOLTAGE
            TimeUnit.MILLISECONDS.sleep(500);
            Calibrate((int)(UVPR), (byte) 0x23);   //PACK UNDERVOLTAGE RELEASE
            TimeUnit.MILLISECONDS.sleep(500);
            sendDataToBLE(OUTfabriekMode);
            busy = false;
            //Show success toast
            Toast.makeText(MainActivity.this,"Calibration Finished!",Toast.LENGTH_LONG).show();
        }

        else{
            Openscan();
        }
    }

    public void Calibrate(int Data, byte register)
    {
        byte[] dataCal = {(byte) ((Data >> 8) & 0xFF), (byte) ((Data >> 0) & 0xff)};
        int Data0_int = (int) dataCal[0]& 0xFF;
        int Data1_int = (int) dataCal[1]& 0xFF;
        int register_int = (int) register& 0xFF;
        int checksum = Data0_int + Data1_int + register_int + 1; //(Data + 0xRegister + Data Length - 1) ^ 0xFFFF
        Log.i(TAG, "Data: " + Data + " ,Data0_int = " + Data0_int + " ,Data1_int= " + Data1_int + " , register_int:" + register_int);
        byte CHECK_L = (byte) (checksum & 0xFF);
        byte CHECK_H = (byte) ((checksum >> 8) & 0xFF);
        //byte[] CHECK = {(byte) ((checksum >> 8) & 0xFF), (byte) (checksum & 0xFF)};
     /*   Log.i(TAG, "dataCal[0]: "+ Integer.toHexString(dataCal[0] & 0xFF)  + ", dataCal[1]: " + Integer.toHexString(dataCal[1] & 0xFF) +
                 ", register: " + Integer.toHexString(register) + ", checksum d: " + checksum + ", int test: " + nice +
                ", CHECK[0]: " + Integer.toHexString((CHECK_H) & 0xFF) + ", CHECK[1]: " + Integer.toHexString((CHECK_L) & 0xFF));*/
        byte[] Data_OUT = {(byte) 0xDD,(byte)  0x5A,(byte) register,(byte)  0x02, dataCal[0] , dataCal[1], (byte) (CHECK_H^0xFF), (byte) (CHECK_L^0xFF), (byte)  0x77, };
        sendDataToBLE(Data_OUT);
    }

}
