package com.Andriod.ER.com;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
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
import java.util.List;
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

    private boolean dummyAdded = false;


    private static final String TAG = DeviceScanActivity.class.getSimpleName();
    private boolean mScanning;
    String selectBattery;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public UUID myisscServiceUUID = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");
    public boolean serviceBool = true;

    private static final int REQUEST_BLUETOOTH_CONNECT = 1001;

    private static final int REQUEST_BLUETOOTH_SCAN = 1002;  // Or any other appropriate integer value
    String unknownDevice = "";
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;

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
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
        mScanCallback = getScanCallback();

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
        dummyAdded = false; // Reset before each scan
        if (!dummyAdded) {
            this.mLeDeviceListAdapter.addDevice(null);  // null = dummy
            dummyAdded = true;
        }
        scanLeDevice(true);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled – start scanning
                scanLeDevice(true);
            } else {
                // User denied Bluetooth – show message and stay here
                Toast.makeText(this, "Bluetooth is required to scan devices.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
            Intent intent = new Intent(this, (Class<?>) MainActivity.class);
            intent.putExtra(MainActivity.EXTRAS_DEVICE_NAME, "ER-Test");
            intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, "A5:C2:37:43:FA:10");
            startActivity(intent);
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

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12+ requires new permissions
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                                != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.BLUETOOTH_CONNECT,
                                    Manifest.permission.ACCESS_FINE_LOCATION // optional fallback
                            }, 1001);
                    return;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android 6+ needs location for BLE
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                    return;
                }
            }

            mScanning = true;

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Scanning...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Scanning", (dialog, which) -> stopScan());
            mProgressDialog.show();

            mHandler.postDelayed(this::stopScan, SCAN_PERIOD);

            // Start scanning
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Modern API 21+
                BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.startScan(getScanFilters(), getScanSettings(), mScanCallback);
                } else {
                    Log.e("BLE", "BluetoothLeScanner is null");
                }
            } else {
                // Legacy API < 21
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }

        } else {
            stopScan();
        }
    }

    private void stopScan() {
        if (mScanning) {
            mScanning = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();
                if (scanner != null) {
                    scanner.stopScan(mScanCallback);
                }
            } else {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    private ScanCallback getScanCallback() {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                runOnUiThread(() -> mLeDeviceListAdapter.addDevice(device));
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e("BLE", "Scan failed: " + errorCode);
            }
        };
    }

    private List<ScanFilter> getScanFilters() {
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(myisscServiceUUID)) // your UUID
                .build());
        return filters;
    }

    private ScanSettings getScanSettings() {
        return new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
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
            if (bluetoothDevice == null && mLeDevices.contains(null)) return;

            if (bluetoothDevice != null) {
                // Remove dummy if it's there
                mLeDevices.remove(null);

                // Avoid duplicates
                for (BluetoothDevice device : mLeDevices) {
                    if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                        return;
                    }
                }
            }

            mLeDevices.add(bluetoothDevice);
            notifyDataSetChanged();
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
            Log.e(DeviceScanActivity.TAG, "Step1");
            if (view == null) {
                view = this.mInflator.inflate(R.layout.listitem_device, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                Log.e(DeviceScanActivity.TAG, "Step2");
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Log.e(DeviceScanActivity.TAG, "Step3");
            BluetoothDevice bluetoothDevice = this.mLeDevices.get(i);
            // Show dummy hardcoded device
            if (bluetoothDevice == null) {
               // viewHolder.deviceName.setText("ER24765019108");
                viewHolder.deviceName.setText("ER-Test");
                viewHolder.deviceAddress.setText("A5:C2:37:43:FA:10");
                return view;
            }

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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted – proceed with scan
                scanLeDevice(true);
            } else {
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_LONG).show();
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
