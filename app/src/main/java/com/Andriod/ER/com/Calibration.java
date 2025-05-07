package com.Andriod.ER.com;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.Andriod.ER.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Calibration extends Activity {
    private final static String TAG = Calibration.class.getSimpleName();

    public byte ReadCommand[];
    public byte LockCommand[];
    public byte UnlockCommand[];
    ;
    public byte CellenReadCommand[];
    public byte HistoryReadCommand[];
    ;;
    //Commands Temps                    ;
    public byte VraagTemp[];
    public byte TempOn1[];
    public byte TempOff1[];
    public byte TempOn2[];
    public byte TempOff2[];
    public byte TempOn3[];
    public byte TempOff3[];
    public byte TempProto1[];
    public byte TempProto2[];
    public byte TempProto3[];
    ;
    public byte fabriekMode[];
    ;
    public byte OUTfabriekMode_read[];
    ;
    public byte OUTfabriekMode[];
    public byte ASK_NC[];
    public byte ASK_UV[];
    public byte ASK_UVP[];
    public boolean packegeReady = false;
    public boolean cellenready = false;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private String mDeviceName;
    private String mDeviceAddress;
    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private static boolean mConnected = false;

    public static boolean getConnectie() {
        return mConnected;
    }

    public int command = 0;
    private boolean ReadyToUnkink = false;
    public boolean Boolforground = true;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;
    public UUID myisscRxUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public UUID myisscTxUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public UUID myisscServiceUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public TextView BatteryNum;
    private BluetoothAdapter mBluetoothAdapter;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "0xffffffff";
    public ImageButton lockButton;
    public Boolean Temp1SwitchState;
    public Boolean Temp2SwitchState;
    public Boolean Temp3SwitchState;
    public Switch Temp1Switch;
    public Switch Temp2Switch;
    public Switch Temp3Switch;
    public EditText Under_Voltage_Pack, Under_Voltage, NomCap, Disamps, Stapms, Champs;
    public TextView Realcurrent;
    public CheckBox ChargeMos, DischargeMos;
    Button firstFragment, secondFragment;
    Button V24, V36, V48;
    public boolean busy = false;
    public int realAmps = 0;
    public TextView percent;
    public TextView voltage;
    public TextView current;
    JSONObject Languages;
    String BLCNotSupported,bluetoothNotSupported="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void RefreshCal(View view) throws InterruptedException {
        // Do something in response to button
        // Get the Intent that started this activity and extract the string

        //*     finding the interface Textview s    */

        if (mConnected == true) {

            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(ReadCommand);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(fabriekMode);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(VraagTemp);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(ASK_NC);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(ASK_UV);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(ASK_UVP);
            TimeUnit.MILLISECONDS.sleep(200);
            sendDataToBLE(OUTfabriekMode_read);
            //realAmps = (int) mBluetoothLeService.Ramps;
            WorkInterface();

            // realAmps = (int) mBluetoothLeService.Ramps;
           /* Bundle b = new Bundle();
            b.putString("message", String.valueOf(mBluetoothLeService.Ramps));
            final FirstFragment First = new FirstFragment();

            First.setArguments(b);
            FragmentManager fm = getFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.add(R.id.frameLayout, First).commit();*/


        } else {
            Openscan();
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void LockCalFunc(View view) throws InterruptedException {
        if (mConnected == true) {

            if (mBluetoothLeService.lockData == 0) {
                TimeUnit.SECONDS.sleep(1);
                sendDataToBLE(LockCommand);
                TimeUnit.SECONDS.sleep(1);
            } else {
                TimeUnit.SECONDS.sleep(1);
                sendDataToBLE(UnlockCommand);
                TimeUnit.SECONDS.sleep(1);
            }

            // WorkInterface();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void OpenCells(View view) throws InterruptedException {
        if (mConnected == true) {

            //final Intent intent = new Intent(this, CellenActivity.class);
            //startActivity(intent);


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void AskTemp() throws InterruptedException {
        sendDataToBLE(fabriekMode);
        TimeUnit.SECONDS.sleep(1);
        sendDataToBLE(VraagTemp);
        TimeUnit.SECONDS.sleep(1);

        WorkInterface();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Temp1(View view) throws InterruptedException {
        if (mConnected == true) {

            if (Temp1Switch.isChecked()) {
                sendDataToBLE(TempProto1);
                TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOn1);
                TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempProto2);
                TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto3);
                // TimeUnit.MILLISECONDS.sleep(200);

            } else {
                sendDataToBLE(TempProto1);
                TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOff1);
                TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempProto2);
                TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto3);
                //  TimeUnit.MILLISECONDS.sleep(200);
            }
            AskTemp();
        } else {
            Openscan();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Temp2(View view) throws InterruptedException {
        if (mConnected == true) {

            if (Temp2Switch.isChecked()) {
                // sendDataToBLE(TempProto1);
                // TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOn2);
                TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto2);
                //TimeUnit.MILLISECONDS.sleep(200);
                //  sendDataToBLE(TempProto3);
                //  TimeUnit.MILLISECONDS.sleep(200);

            } else {
                //  sendDataToBLE(TempProto1);
                // TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOff2);
                TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto2);
                //  TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto3);
                // TimeUnit.MILLISECONDS.sleep(200);
            }
            AskTemp();

        } else {
            Openscan();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Temp3(View view) throws InterruptedException {
        if (mConnected == true) {

            if (Temp3Switch.isChecked()) {
                // sendDataToBLE(TempProto1);
                // TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOn3);
                TimeUnit.MILLISECONDS.sleep(200);
                //  sendDataToBLE(TempProto2);
                // TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto3);
                // TimeUnit.MILLISECONDS.sleep(200);

            } else {
                //  sendDataToBLE(TempProto1);
                // TimeUnit.MILLISECONDS.sleep(200);
                sendDataToBLE(TempOff3);
                TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto2);
                // TimeUnit.MILLISECONDS.sleep(200);
                // sendDataToBLE(TempProto3);
                // TimeUnit.MILLISECONDS.sleep(200);
            }
            AskTemp();

        } else {
            Openscan();
        }
    }

    public void Cal_NC(View view) throws InterruptedException {

    }

    public void Cal_STamps(View view) throws InterruptedException {

    }

    public void Cal_Disamps(View view) throws InterruptedException {

    }

    public void Cal_CHamps(View view) throws InterruptedException {

    }

    public void Cal_UVP(View view) throws InterruptedException {

    }

    public void Cal_UV(View view) throws InterruptedException {

    }


    public void Link(View view) {
        if (mConnected == true) {
            unbindService(mServiceConnection);
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
            mConnected = false;
            ClearInterface();
            // startService(new Intent(view.getContext(),ForegroundService.class));
            Openscan();
        } else {
            // startService(new Intent(view.getContext(),ForegroundService.class));
            Openscan();

            //simulation mode
            // final Intent intent = new Intent(this, ScanSimulator.class);
            // startActivity(intent);
        }
    }

    public void UnLink(View view) {
        // Do something in response to button
        if (mConnected == true) {
            unbindService(mServiceConnection);
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
            mConnected = false;
            ClearInterface();
            //stopThread();
            //Openscan();
        } else {
            // stopThread();
            //Openscan();
        }


    }

    public void Cal_24V_100Ah(View view) throws InterruptedException {

    }

    public void Cal_48V_100Ah(View view) throws InterruptedException {

    }

    public void Cal_36V_100Ah(View view) throws InterruptedException {

    }

    public void Openscan() {
        final Intent intent = new Intent(this, DeviceScanActivity.class);
        startActivity(intent);
    }

    public void startGround() {
        Log.i(TAG, "Functie startGround..");

        final Intent intent = new Intent(this, ForegroundService.class);
        startService(intent);
        // Should we show an explanation?
        // Here, thisActivity is the current activity
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void WorkInterface() {
        /*TextView percent = findViewById(R.id.Percentage);
        TextView voltage = findViewById(R.id.Voltage);
        TextView current = findViewById(R.id.Current);
        TextView time = findViewById(R.id.Time);
        TextView temp = findViewById(R.id.Temperature);
        TextView BatteryNum = findViewById(R.id.BatteryNo);
*/
        BatteryNum.setText(mDeviceName);
        percent.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.percentage) + "%"));
        voltage.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.volts) + "V"));
        current.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.Ramps) + "A"));
        //Normal Calibration interface
     /*   Realcurrent.setText(String.valueOf(new DecimalFormat("##.#").format(mBluetoothLeService.Ramps) + "A"));
        if(mBluetoothLeService.lockData == 0) {
            lockButton.setImageResource(R.drawable.unlock_10);
        }
        else if((mBluetoothLeService.lockData == 1)) {
            lockButton.setImageResource(R.drawable.lock_11);
        }

        if(mBluetoothLeService.Temp_Sensors_Data == 3)
        {
            Temp1Switch.setChecked(true);
            Temp2Switch.setChecked(true);

        }
        else if(mBluetoothLeService.Temp_Sensors_Data == 2)
        {
            Temp1Switch.setChecked(false);
            Temp2Switch.setChecked(true);

        }
        else if(mBluetoothLeService.Temp_Sensors_Data == 1)
        {
            Temp1Switch.setChecked(true);
            Temp2Switch.setChecked(false);

        }

        else if(mBluetoothLeService.Temp_Sensors_Data == 0)
        {
            Temp1Switch.setChecked(false);
            Temp2Switch.setChecked(false);

        }

        NomCap.setText(String.valueOf((int)mBluetoothLeService.NomCap_Par));
        Under_Voltage.setText(String.valueOf((float) mBluetoothLeService.UV_Par));
        Under_Voltage_Pack.setText(String.valueOf((float) mBluetoothLeService.UVP_Par));

        if (mBluetoothLeService.Mosfets == 0x3)
        {
            ChargeMos.setChecked(true);
            DischargeMos.setChecked(true);
        }
        else if (mBluetoothLeService.Mosfets == 0x2)
        {
            ChargeMos.setChecked(false);
            DischargeMos.setChecked(true);
        }
        else if (mBluetoothLeService.Mosfets == 0x1)
        {
            ChargeMos.setChecked(true);
            DischargeMos.setChecked(false);
        }
        else if (mBluetoothLeService.Mosfets == 0x0)
        {
            ChargeMos.setChecked(false);
            DischargeMos.setChecked(false);
        }*/
        mBluetoothLeService.DataReady = false;
    }

    public void ClearInterface() {
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
        V24.setBackgroundColor(Color.LTGRAY);
        V36.setBackgroundColor(Color.LTGRAY);
        V48.setBackgroundColor(Color.LTGRAY);
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
                //updateConnectionState(R.string.connected);
                // invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mConnected = false;
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        /*Intent mainIntent = new Intent(MainActivity.this, SplashActivity.class);
        MainActivity.this.startActivity(mainIntent);*/
        setContentView(R.layout.defaultcalibration);

        //fragment
      /*  firstFragment = (Button) findViewById(R.id.firstFragment);
        secondFragment = (Button) findViewById(R.id.secondFragment);

        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load First Fragment
                // create a FragmentManager
                FragmentManager fm = getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.frameLayout, new FirstFragment());
                fragmentTransaction.commit(); // save the changes

            }
        });*/
// perform setOnClickListener event on Second Button
     /*   secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load Second Fragment
                // create a FragmentManager
                FragmentManager fm = getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(R.id.frameLayout, new SecondFragment());
                fragmentTransaction.commit(); // save the changes
            }
        });*/


        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        BatteryNum = findViewById(R.id.BatteryNoCal);
        BatteryNum.setText("Energy-Research");

        V24 = findViewById(R.id.button_24V);
        V36 = findViewById(R.id.button_36V);
        V48 = findViewById(R.id.button_48V);
        percent = findViewById(R.id.Percentagecal);
        voltage = findViewById(R.id.Voltagecal);
        current = findViewById(R.id.Currentcal);

        percent.bringToFront();
        voltage.bringToFront();
        current.bringToFront();
        //Normal Calibration
       /* lockButton = findViewById(R.id.LockbutCal);
        // initiate a Switch
        Temp1Switch = (Switch) findViewById(R.id.switchTemp1);
        Temp2Switch = (Switch) findViewById(R.id.switchTemp2);
        Temp3Switch = (Switch) findViewById(R.id.switchTemp3);

        NomCap = (EditText) findViewById(R.id.editNomCap);
        Disamps = (EditText) findViewById(R.id.editDISamps);
        Champs = (EditText) findViewById(R.id.editCHamps);
        Stapms = (EditText) findViewById(R.id.editSTaps);
        Under_Voltage_Pack = (EditText) findViewById(R.id.edituvp);
        Under_Voltage = (EditText) findViewById(R.id.editUV);
        Realcurrent = (TextView) findViewById(R.id.realamps);
        ChargeMos = findViewById(R.id.ChMosFit);
        DischargeMos = findViewById(R.id.DisMosfit);

        ChargeMos.setChecked(true);
       DischargeMos.setChecked(false);

        NomCap.setTextColor(Color.RED);
        Disamps.setTextColor(Color.RED);
        Champs.setTextColor(Color.RED);
        Stapms.setTextColor(Color.RED);
        Under_Voltage_Pack.setTextColor(Color.RED);
        //Under_Voltage_Pack.setEnabled(true);
        Under_Voltage.setTextColor(Color.RED);
        //Under_Voltage.setEnabled(true);

// check current state of a Switch (true or false).
         Temp1SwitchState = Temp1Switch.isChecked();
         Temp2SwitchState = Temp2Switch.isChecked();
         Temp3SwitchState = Temp3Switch.isChecked();
         Temp1Switch.setChecked(true);
        Temp2Switch.setChecked(true);
         Temp3Switch.setChecked(true);
*/ //Normal Calibration above
        //To solve issue : Android EditText typed text not showing when keyboard appears in Fragment
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );

        ClearInterface();
        startGround();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        startThread();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(this,BLCNotSupported , Toast.LENGTH_SHORT).show();
            //Intent i =new Intent(DeviceScanActivity.this,MainActivity.class);
            // startActivity(i);
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this,bluetoothNotSupported , Toast.LENGTH_SHORT).show();
            //Intent i =new Intent(DeviceScanActivity.this,MainActivity.class);
            // startActivity(i);
            return;
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //if (ContextCompat.checkSelfPermission(this,
                //             Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                //             == PackageManager.PERMISSION_GRANTED)
                // {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }

                // }

                //     else {   ActivityCompat.requestPermissions(this,
                //      new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                //      MY_PERMISSIONS_REQUEST_LOCATION);
                // }
            }

            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    public void startThread() {
        MyThread thread = new MyThread();
        thread.start();
    }

    public void stopThread() {
        MyThread thread = new MyThread();
        thread.stop();
    }


    public class MyThread extends Thread {


        MyThread() {

        }

        @Override
        public void run() {
            Log.i(TAG, "MyThread running");
            while (true) {

                if (!mConnected) {
                } else if (mConnected) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendDataToBLE(ReadCommand);
                    //sendDataToBLE(CellenReadCommand);
                    try {
                        TimeUnit.SECONDS.sleep(1);
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


    void sendDataToBLE(byte str[]) {

        final byte[] data = str;
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            encodeHexString(data);

        }
        if (mConnected) {
            characteristicTX.setValue(str);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }

    public String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(String.format("%02X ", byteArray[i]));
        }
        Log.i(TAG, "Data hexString out:" + hexStringBuffer.toString());
        return hexStringBuffer.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, " Back to MainActivity..");
        if (EXTRAS_DEVICE_ADDRESS != null) {
            Log.i(TAG, " Connection..");
            //  mBluetoothLeService.connect(EXTRAS_DEVICE_ADDRESS);
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.i(TAG, "Connect request result=" + result);

        }
        try {
            Languages = new JSONObject(SharedHelper.getKey(Calibration.this, "Languages"));
            BLCNotSupported = Languages.optString("BLCNotSupported");
            bluetoothNotSupported = Languages.optString("bluetoothNotSupported");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;

    }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1000139:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case 1000072:
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
                if (mConnected) {
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

    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}


