package com.Andriod.ER.com;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import com.Andriod.ER.R;
import com.Andriod.ER.com.BluetoothLeService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    private static final int BACKGROUND_LOCATION_PERMISSION_CODE = 125;
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "0xffffffff";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    private static final int LOCATION_PERMISSION_CODE = 126;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private static final int MY_PERMISSIONS_REQUEST_CODE_Scan_Connect = 124;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    public TextView BatteryNum;
    JSONObject Languages;
    public TextView Warning;
    String accessToBluetoothAndLocationAreRequired;
    String addBattery;
    private FloatingActionButton addBatteryBtn;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapterBatteries;
    String bluetoothNotSupported;
    String cancel;
    private Button changeLanBtn;
    String changeLanguage;
    String changePassword;
    private BluetoothGattCharacteristic characteristicRX;
    private BluetoothGattCharacteristic characteristicTX;
    long count;
    public TextView current;
    private JSONArray data;
    String locationPermissionDenied;
    String locationPermissionGranted;
    public ImageButton lockButton;
    String logout;
    private FloatingActionButton logoutBtn;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private Button mBtnDoTask;
    private String mDeviceAddress;
    private String mDeviceName;
    private ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;
    NavigationView navigationView;
    String ok;
    public TextView percent;
    String pleaseGrantThosePermissions;
    String scanConnectPermissionDenied;
    String scanConnectPermissionGranted;
    String selectOneLanguage;
    TextView switchBtnOn_txtView;
    TextView switchBtnoff_txtView;
    public TextView temp;
    public TextView time;
    private TextView toggleSwitch;
    String viewBatteries;
    public TextView voltage;
    String yourSelectedLanguageIs;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static boolean mConnected = false;
    private static boolean FinalMconected = false;
    private static boolean FinalMconected2 = false;
    public boolean busy = false;
    public byte[] ReadCommand = {-35, -91, 3, 0, -1, -3, 119, 13, 10};
    public byte[] LockCommand = {-35, 90, -31, 2, 0, 2, -1, 27, 119};
    public byte[] UnlockCommand = {-35, 90, -31, 2, 0, 0, -1, 29, 119};
    public byte[] CellenReadCommand = {-35, -91, 4, 0, -1, -4, 119, 13, 10};
    public byte[] HistoryReadCommand = {-35, -86, 119};
    public byte[] fabriekMode = {-35, 90, 0, 2, 86, 120, -1, 48, 119};
    public byte[] OUTfabriekMode_read = {-35, 90, 1, 2, 0, 0, -1, -3, 119};
    public byte[] OUTfabriekMode = {-35, 90, 1, 2, 40, 40, -1, -83, 119};
    public byte[] ASK_UV = {-35, -91, 38, 0, -1, -38, 119};
    public UUID myisscRxUUID = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    public UUID myisscTxUUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    public UUID myisscServiceUUID = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public String Warning_String = "";
    public Boolean calibraiton_checked = false;
    public int counter_calibraiton_checked = 0;
    public float Call_UV_save = 3.175f;
    public float Call_UV_r_save = 3.25f;
    public float Call_UV_reserve = 2.85f;
    public float Call_UV_r_reserve = 3.15f;
    public float Call_UV_boost = 2.65f;
    public float Call_UV_r_boost = 3.25f;
    public float Cell_UV = 0.0f;
    public float Cell_UV_r = 0.0f;
    public int periodic_calibraiton_check = 0;
    String myBatteries = "";
    public BatteryNames[] BatteryList_2 = new BatteryNames[10];
    int NumMyBatteries = 0;
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.Andriod.ER.com.MainActivity.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MainActivity.this.mBluetoothLeService = ((BluetoothLeService.LocalBinder) iBinder).getService();
            if (!MainActivity.this.mBluetoothLeService.initialize()) {
                Log.e(MainActivity.TAG, "Unable to initialize Bluetooth");
                MainActivity.this.finish();
            }
            MainActivity.this.mBluetoothLeService.connect(MainActivity.this.mDeviceAddress);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            MainActivity.this.mBluetoothLeService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.Andriod.ER.com.MainActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                boolean unused = MainActivity.mConnected = true;
                MainActivity mainActivity = MainActivity.this;
                mainActivity.mProgressDialog = ProgressDialog.show(mainActivity, "Connecting! ", "Please wait...", false, false);
                Log.i(MainActivity.TAG, "mConnected = true; ");
                return;
            }
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                boolean unused2 = MainActivity.FinalMconected2 = true;
                Log.i(MainActivity.TAG, "FinalMconected2 = true; ");
                return;
            }
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                boolean unused3 = MainActivity.mConnected = false;
                boolean unused4 = MainActivity.FinalMconected = false;
                boolean unused5 = MainActivity.FinalMconected2 = false;
                Log.i(MainActivity.TAG, "mConnected = false; ");
                return;
            }
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action) && MainActivity.mConnected) {
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.displayGattServices(mainActivity2.mBluetoothLeService.getSupportedGattServices());
            }
        }
    };

    public static boolean getConnectie() {
        return mConnected;
    }

    public class BatteryNames {
        String deviceName;

        public BatteryNames() {
        }
    }

    public void Refresh(View view) throws InterruptedException {
        if (mConnected) {
            sendDataToBLE(this.ReadCommand);
            TimeUnit.MILLISECONDS.sleep(500L);
            sendDataToBLE(this.CellenReadCommand);
            TimeUnit.MILLISECONDS.sleep(200L);
            WorkInterface();
            return;
        }
        if (this.mBluetoothLeService.mConnectionState == 0) {
            Openscan();
        }
    }

    public void Lock(View view) throws InterruptedException {
        if (mConnected) {
            this.busy = true;
            if (this.mBluetoothLeService.lockData == 0.0f) {
                TimeUnit.SECONDS.sleep(1L);
                sendDataToBLE(this.LockCommand);
                TimeUnit.SECONDS.sleep(1L);
            } else {
                TimeUnit.SECONDS.sleep(1L);
                sendDataToBLE(this.UnlockCommand);
                TimeUnit.SECONDS.sleep(1L);
            }
            this.busy = false;
            WorkInterface();
        }
    }

    private void disconnect_BLE() {
        BluetoothLeService bluetoothLeService;
        if (this.mBluetoothAdapter == null || (bluetoothLeService = this.mBluetoothLeService) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothLeService.disconnect();
        this.mBluetoothLeService.close();
        mConnected = false;
        FinalMconected = false;
        this.characteristicTX = null;
        ClearInterface();
    }

    public void Func_inform_Warning() {
        this.Warning.setVisibility(View.INVISIBLE);
        this.Warning_String = "";
        if (this.mBluetoothLeService.SOV == 1) {
            this.Warning_String += "Battery is fully charged, please stop the charge? \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.SUV == 1) {
            this.Warning_String += "Battery is fully discharged, please stop the use and start your charge as soon as possible? \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.POV == 1) {
            this.Warning_String += "Battery is fully charged, please stop the charge? \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.PUV == 1) {
            this.Warning_String += "Battery is fully discharged, please stop the use and start your charge as soon as possible? \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.COT == 1) {
            this.Warning_String += "Please stop charge? Battery is overheating! \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.CUT == 1) {
            this.Warning_String += "Please warm up the battery so it can safely charge because the battery is to Cold! \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.DOT == 1) {
            this.Warning_String += "Please stop charge? Battery is overheating! \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.DUT == 1) {
            this.Warning_String += "Please warm up the battery so it can safely charge because the battery is to Cold! \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.COC == 1) {
            this.Warning_String += "Please lower the amount of amps you are currently charging with to max 40Amp \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.DOC == 1) {
            this.Warning_String += "Please lower the amount of amps you are currently discharging? \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.SC == 1) {
            this.Warning_String += "Short circuit protection, disconnect and check your wiring! \n";
            this.Warning.setVisibility(android.view.View.VISIBLE);
        }
        if (this.mBluetoothLeService.IC_Error == 1) {
            this.Warning_String += "Internal Error! Please contact your supplier! \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        if (this.mBluetoothLeService.volts < this.mBluetoothLeService.AantalCellen * this.Call_UV_r_save && this.mBluetoothLeService.Ramps < 1.0f) {
            this.Warning_String += "Please remember to charge your battery to 100%! \n";
            this.Warning.setVisibility(View.VISIBLE);
        }
        this.Warning.setText(this.Warning_String);
    }

    public void Proaction_state_func(View view) {
        if (mConnected) {
            Bundle bundle = new Bundle();
            bundle.putIntArray("protection_state", new int[]{this.mBluetoothLeService.SOV, this.mBluetoothLeService.SUV, this.mBluetoothLeService.POV, this.mBluetoothLeService.PUV, this.mBluetoothLeService.COT, this.mBluetoothLeService.CUT, this.mBluetoothLeService.DOT, this.mBluetoothLeService.DUT, this.mBluetoothLeService.COC, this.mBluetoothLeService.DOC, this.mBluetoothLeService.SC, this.mBluetoothLeService.IC_Error});
            Protaction_Activity protaction_Activity = new Protaction_Activity();
            protaction_Activity.setArguments(bundle);
            protaction_Activity.show(getSupportFragmentManager(), "Protection State");
        }
    }

    public void Link(View view) {
        if (mConnected) {
            disconnect_BLE();
            if (this.mBluetoothLeService.mConnectionState == 0) {
                Openscan();
                return;
            }
            return;
        }
        Openscan();
    }

    public void UnLink(View view) {
        if (mConnected) {
            disconnect_BLE();
        }
    }

    public void Openscan() {
        startActivity(new Intent(this, (Class<?>) DeviceScanActivity.class));
    }

    public void startGround() {
        Log.i(TAG, "Functie startGround..");
        startService(new Intent(this, (Class<?>) ForegroundService.class));
    }

    public void WorkInterface() {
        if (this.busy) {
            return;
        }
        this.mProgressDialog.dismiss();
        this.BatteryNum.setText(this.mDeviceName);
        if (this.mBluetoothLeService.ReadComand_ready) {
            this.percent.setText(String.valueOf(new DecimalFormat("##.#").format(this.mBluetoothLeService.percentage) + "%"));
            this.voltage.setText(String.valueOf(new DecimalFormat("##.#").format((double) this.mBluetoothLeService.volts) + "V"));
            this.current.setText(String.valueOf(new DecimalFormat("##.#").format((double) this.mBluetoothLeService.Ramps) + "A"));
            this.time.setText(String.valueOf(new DecimalFormat("##.#").format((double) this.mBluetoothLeService.time) + "h"));
            this.temp.setText(String.valueOf(new DecimalFormat("##.#").format((double) this.mBluetoothLeService.temp1) + "°C"));
            this.mProgressBar.setProgress((int) this.mBluetoothLeService.percentage);
            if (this.mBluetoothLeService.percentage <= 30.0f) {
                this.mProgressBar.setProgressTintList(ColorStateList.valueOf(0xFFFF0000));
            } else if (this.mBluetoothLeService.percentage > 30.0f && this.mBluetoothLeService.percentage < 40.0f) {
                this.mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(255, 165, 0)));
            } else if (this.mBluetoothLeService.percentage >= 40.0f) {
                this.mProgressBar.setProgressTintList(ColorStateList.valueOf(-16711936));
            }
            Func_inform_Warning();
            if (this.mBluetoothLeService.lockData == 0.0f) {
                this.lockButton.setImageResource(R.drawable.unlock_10);
            } else if (this.mBluetoothLeService.lockData == 1.0f) {
                this.lockButton.setImageResource(R.drawable.lock_11);
            }
            this.mBluetoothLeService.ReadComand_ready = false;
        }
        this.mBluetoothLeService.DataReady = false;
    }

    public void ClearInterface() {
        this.Warning_String = "";
        this.Warning.setVisibility(View.INVISIBLE);
        this.BatteryNum.setText("Energy-Research");
        this.percent.setText("00.0%");
        this.voltage.setText("00.0V");
        this.current.setText("00.0A");
        this.time.setText("00.0h");
        this.temp.setText("00.0°C");
        this.mProgressBar.setProgress(0);
        this.Warning.setVisibility(View.INVISIBLE);
    }

    private void checkPermission111() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return;
        }
        askForLocationPermission();
    }

    private void askForLocationPermission() {
        new AlertDialog.Builder(this).setTitle("Permission Needed!").setMessage("Location Permission needed to enable bluetooth scanning and connectivity features!, please tap \"While using this app\"").setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 126);
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    private void askPermissionForBackgroundUsage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
            new AlertDialog.Builder(this).setTitle("Permission Needed!").setMessage("Background Location Permission needed to enable bluetooth scanning and connectivity features to monitor your battery!, please tap \"Allow all time in the next screen\"").setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, 125);
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_BACKGROUND_LOCATION"}, 125);
        }
    }

    protected void checkPermission12() {
        Log.i(TAG, "checkPermission12");
        if (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") == 0) {
            return;
        }
        Log.i(TAG, "!= PackageManager.PERMISSION_GRANTED");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Energy Research needs permission to access the Bluetooth scanning and connectivity features to find nearby ER batteries and connect to them!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= 31) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT"}, 124);
                }
                dialogInterface.cancel();
                MainActivity.this.busy = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.MainActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                MainActivity.this.busy = false;
            }
        });
        builder.create().show();
    }

    protected void checkPermission() {
        if (Build.VERSION.SDK_INT >= 31) {
            checkPermission12();
        } else {
            checkPermission111();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        mConnected = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        View test = findViewById(R.id.Warning);
        if (test == null) {
            Log.e("MainActivity", "Warning TextView is NULL");
        } else {
            Log.d("MainActivity", "Warning TextView found: " + test.toString());
        }
        Intent intent = getIntent();
        TextView textView = (TextView) findViewById(R.id.Warning);
        this.Warning = textView;
        textView.setVisibility(View.INVISIBLE);
        this.mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        this.mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        TextView textView2 = (TextView) findViewById(R.id.BatteryNo);
        this.BatteryNum = textView2;
        textView2.setText("Energy-Research");
        this.percent = (TextView) findViewById(R.id.Percentage);
        this.voltage = (TextView) findViewById(R.id.Voltage);
        this.current = (TextView) findViewById(R.id.Current);
        this.time = (TextView) findViewById(R.id.Time);
        this.temp = (TextView) findViewById(R.id.Temperature);
        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.lockButton = (ImageButton) findViewById(R.id.Lockbut);
        this.percent.bringToFront();
        this.voltage.bringToFront();
        this.current.bringToFront();
        this.time.bringToFront();
        this.temp.bringToFront();
        ClearInterface();
        bindService(new Intent(this, (Class<?>) BluetoothLeService.class), this.mServiceConnection, 1);
        startThread();
        checkPermission();
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, this.bluetoothNotSupported, Toast.LENGTH_SHORT).show();
        }
        BluetoothAdapter adapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter == null) {
            Toast.makeText(this, this.bluetoothNotSupported, Toast.LENGTH_SHORT).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        switch (i) {
            case 124:
                if (iArr.length > 0 && iArr[0] == 0) {
                    Toast.makeText(this, "Scan Connect Permission Granted", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Toast.makeText(this, "Scan Connect Permission Denied", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 125:
                if (iArr[0] == 0) {
                    Toast.makeText(this, "BACKGROUND Location Permission Granted", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Toast.makeText(this, "BACKGROUND Location Permission Denied", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 126:
                if (iArr[0] == 0) {
                    Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
                    checkPermission111();
                    break;
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    public void startThread() {
        new MyThread().start();
    }

    public void stopThread() {
        new MyThread().stop();
    }

    public class MyThread extends Thread {
        MyThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                if (!MainActivity.this.busy && MainActivity.mConnected && MainActivity.mConnected && MainActivity.FinalMconected && MainActivity.this.characteristicTX != null) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.sendDataToBLE(mainActivity.ReadCommand);
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.sendDataToBLE(mainActivity2.CellenReadCommand);
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    if (MainActivity.this.mBluetoothLeService.AantalCellen != 0) {
                        MainActivity.this.updateInterFace();
                    }
                }
            }
        }
    }

    void sendDataToBLE(byte[] bArr) {
        if (this.characteristicTX != null) {
            if (bArr != null && bArr.length > 0) {
                StringBuilder sb = new StringBuilder(bArr.length);
                for (byte b : bArr) {
                    sb.append(String.format("%02X ", Byte.valueOf(b)));
                }
                encodeHexString(bArr);
            }
            if (mConnected && FinalMconected) {
                this.characteristicTX.setValue(bArr);
                this.mBluetoothLeService.writeCharacteristic(this.characteristicTX);
                this.mBluetoothLeService.setCharacteristicNotification(this.characteristicRX, true);
            }
        }
    }

    public String encodeHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(String.format("%02X ", Byte.valueOf(b)));
        }
        Log.i(TAG, "Data hexString out:" + stringBuffer.toString());
        return stringBuffer.toString();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Log.i(TAG, " Back to MainActivity..");
        Log.i(TAG, " Connection..");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter(), null,null,Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }

        BluetoothLeService bluetoothLeService = this.mBluetoothLeService;
        if (bluetoothLeService != null) {
            boolean connect = bluetoothLeService.connect(this.mDeviceAddress);
            Log.i(TAG, "Connect request result=" + connect);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this.mServiceConnection);
        this.mBluetoothLeService = null;
    }

    @Override // android.app.Activity
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

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInterFace() {
        runOnUiThread(new Runnable() { // from class: com.Andriod.ER.com.MainActivity.9
            @Override // java.lang.Runnable
            public void run() {
                Log.i(MainActivity.TAG, "updateInterFace");
                if (MainActivity.mConnected && MainActivity.FinalMconected && !MainActivity.this.busy && MainActivity.this.mBluetoothLeService.DataReady) {
                    MainActivity.this.WorkInterface();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayGattServices(List<BluetoothGattService> list) {
        if (list == null) {
            return;
        }
        getResources().getString(R.string.unknown_service);
        new ArrayList();
        for (BluetoothGattService bluetoothGattService : list) {
            UUID uuid = bluetoothGattService.getUuid();
            Log.i(TAG, "UUIDService:" + String.valueOf(uuid));
            if (uuid.equals(this.myisscServiceUUID)) {
                Log.i(TAG, "Service Descoverd");
                this.characteristicTX = bluetoothGattService.getCharacteristic(this.myisscTxUUID);
                Log.i(TAG, "UUIDTX:" + String.valueOf(this.characteristicTX));
                this.characteristicRX = bluetoothGattService.getCharacteristic(this.myisscRxUUID);
                Log.i(TAG, "UUIDRX:" + String.valueOf(this.characteristicRX));
            }
        }
        FinalMconected = true;
        this.busy = false;
        Log.i(TAG, "FinalMconected = true;");
    }

    public void open_Dialog_Cells(View view) throws InterruptedException {
        if (mConnected) {
            Bundle bundle = new Bundle();
            bundle.putFloatArray("cells", new float[]{this.mBluetoothLeService.AantalCellen, this.mBluetoothLeService.cel1, this.mBluetoothLeService.cel2, this.mBluetoothLeService.cel3, this.mBluetoothLeService.cel4, this.mBluetoothLeService.cel5, this.mBluetoothLeService.cel6, this.mBluetoothLeService.cel7, this.mBluetoothLeService.cel8, this.mBluetoothLeService.cel9, this.mBluetoothLeService.cel10, this.mBluetoothLeService.cel11, this.mBluetoothLeService.cel12, this.mBluetoothLeService.cel13, this.mBluetoothLeService.cel14, this.mBluetoothLeService.cel15, this.mBluetoothLeService.cel16});
            bundle.putString("mDeviceName", this.mDeviceName);
            CellenDialog cellenDialog = new CellenDialog();
            cellenDialog.setArguments(bundle);
            cellenDialog.show(getSupportFragmentManager(), "Battery Cells");
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
