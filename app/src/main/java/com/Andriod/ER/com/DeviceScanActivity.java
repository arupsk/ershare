package com.Andriod.ER.com;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.Andriod.ER.R;
import java.util.ArrayList;
import java.util.UUID;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeviceScanActivity extends ListActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    JSONObject Languages;
    private BluetoothAdapter mBluetoothAdapter;
    public BluetoothLeService mBluetoothLeService;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ProgressDialog mProgressDialog;
    private boolean mScanning;
    String selectBattery;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public UUID myisscServiceUUID = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");
    public boolean serviceBool = true;

    private static final int REQUEST_BLUETOOTH_CONNECT = 1001;

    private static final int REQUEST_BLUETOOTH_SCAN = 1002;  // Or any other appropriate integer value
    String unknownDevice = "";
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() { // from class: com.Andriod.ER.com.DeviceScanActivity.3
        void AnonymousClass3() {
        }

        /* renamed from: com.Andriod.ER.com.DeviceScanActivity$3$1 */

        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            DeviceScanActivity.this.runOnUiThread(new Runnable() { // from class: com.Andriod.ER.com.DeviceScanActivity.3.1

                @Override // java.lang.Runnable
                public void run() {
                    DeviceScanActivity.this.mLeDeviceListAdapter.addDevice(bluetoothDevice);
                    DeviceScanActivity.this.mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public void Rescan(View view) {
        this.mProgressDialog.dismiss();
        if (!this.mBluetoothAdapter.isEnabled() && !this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();
        this.mLeDeviceListAdapter = leDeviceListAdapter;
        setListAdapter(leDeviceListAdapter);
        this.mLeDeviceListAdapter.clear();
        scanLeDevice(true);
    }

    public void Cancel(View view) {
        this.mProgressDialog.dismiss();
        scanLeDevice(false);
        startActivity(new Intent(this, (Class<?>) MainActivity.class));
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActionBar().setTitle(this.selectBattery);
        this.mHandler = new Handler();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setElevation(0.0f);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        this.mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        if (!this.mBluetoothAdapter.isEnabled() && !this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();
        this.mLeDeviceListAdapter = leDeviceListAdapter;
        setListAdapter(leDeviceListAdapter);
        this.mLeDeviceListAdapter.clear();
        scanLeDevice(true);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == 0) {
            startActivity(new Intent(this, (Class<?>) MainActivity.class));
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }

    @Override // android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView listView, View view, int i, long j) {
        BluetoothDevice device = this.mLeDeviceListAdapter.getDevice(i);
        if (device == null) {
            return;
        }
        if (this.mScanning) {
            scanLeDevice(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        1001 // Request code
                );
                return; // ❗️Don't continue until permission result is handled
            }
        }
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        intent.putExtra(MainActivity.EXTRAS_DEVICE_NAME, device.getName());
        intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        startActivity(intent);
    }

    private void scanLeDevice(boolean z) {
        if (z) {
            this.mScanning = true;
            ProgressDialog progressDialog = new ProgressDialog(this);
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage("Scanning...");
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setButton(-2, "Stop Scanning", new DialogInterface.OnClickListener() { // from class: com.Andriod.ER.com.DeviceScanActivity.1
                void AnonymousClass1() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (ContextCompat.checkSelfPermission(DeviceScanActivity.this, Manifest.permission.BLUETOOTH_SCAN)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(DeviceScanActivity.this,
                                    new String[]{ Manifest.permission.BLUETOOTH_CONNECT,
                                            Manifest.permission.BLUETOOTH_SCAN,
                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                    1001);
                            return; // Wait for permission result
                        }
                    }
                    DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
                    DeviceScanActivity.this.mProgressDialog.dismiss();
                }
            });
            this.mProgressDialog.show();
            this.mBluetoothAdapter.startLeScan(new UUID[]{this.myisscServiceUUID}, this.mLeScanCallback);
            this.mHandler.postDelayed(new Runnable() { // from class: com.Andriod.ER.com.DeviceScanActivity.2
                void AnonymousClass2() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    DeviceScanActivity.this.mProgressDialog.dismiss();
                    DeviceScanActivity.this.mScanning = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (ContextCompat.checkSelfPermission(DeviceScanActivity.this, Manifest.permission.BLUETOOTH_SCAN)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(DeviceScanActivity.this,
                                    new String[]{
                                            Manifest.permission.BLUETOOTH_CONNECT,
                                            Manifest.permission.BLUETOOTH_SCAN,
                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                    1001);
                            return; // Wait for permission result
                        }
                    }
                    DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
                }
            }, SCAN_PERIOD);
            return;
        }
        this.mProgressDialog.dismiss();
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
    }

    /* renamed from: com.Andriod.ER.com.DeviceScanActivity$1 */
    class AnonymousClass1 implements DialogInterface.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(DeviceScanActivity.this, Manifest.permission.BLUETOOTH_SCAN)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeviceScanActivity.this,
                            new String[]{
                                    Manifest.permission.BLUETOOTH_CONNECT,
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.ACCESS_FINE_LOCATION },
                            1001);
                    return; // Wait for permission result
                }
            }
            DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
            DeviceScanActivity.this.mProgressDialog.dismiss();
        }
    }

    /* renamed from: com.Andriod.ER.com.DeviceScanActivity$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DeviceScanActivity.this.mProgressDialog.dismiss();
            DeviceScanActivity.this.mScanning = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(DeviceScanActivity.this, Manifest.permission.BLUETOOTH_SCAN)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeviceScanActivity.this,
                            new String[]{
                                    Manifest.permission.BLUETOOTH_CONNECT,
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            1001);
                    return; // Wait for permission result
                }
            }
            DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public LeDeviceListAdapter() {
            this.mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice bluetoothDevice) {
            if (this.mLeDevices.contains(bluetoothDevice)) {
                return;
            }
            this.mLeDevices.add(bluetoothDevice);
        }

        public BluetoothDevice getDevice(int i) {
            return this.mLeDevices.get(i);
        }

        public void clear() {
            this.mLeDevices.clear();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mLeDevices.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.mLeDevices.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflator.inflate(R.layout.listitem_device, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice bluetoothDevice = this.mLeDevices.get(i);

            String name = bluetoothDevice.getName();
            if (name != null && name.length() > 0) {
                viewHolder.deviceName.setText(name);
            } else {
                viewHolder.deviceName.setText(DeviceScanActivity.this.unknownDevice);
            }
            viewHolder.deviceAddress.setText(bluetoothDevice.getAddress());
            return view;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                // Permissions granted: You can now allow the user to re-select the device
                Toast.makeText(this, "Bluetooth permissions granted. Please select the device again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bluetooth permissions denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
        }
    }
}
