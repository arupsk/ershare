package com.Andriod.ER.com;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public class BluetoothLeService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    public UUID YOUR_BATTERY_SERVICE_UUID  = UUID.fromString("0000ff00-0000-1000-8000-00805f9b34fb");

    public UUID YOUR_BATTERY_LEVEL_CHARACTERISTIC_UUID   = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = BluetoothLeService.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    public int mConnectionState = 0;
    public boolean cellenDataReady = false;
    byte[] one = new byte[0];
    byte[] two = new byte[0];
    byte[] combined = new byte[0];
    public float volts = 0.0f;
    public float amps = 0.0f;
    public float Ramps = 0.0f;
    public float percentage = 0.0f;
    public float temp1 = 0.0f;
    public float temp2 = 0.0f;
    public float time = 0.0f;
    public float RemainCap = 0.0f;
    public float NomCap = 0.0f;
    public int AantalCellen = 0;
    public double[] cellen = {0.0d};
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
    public float lockData = 0.0f;
    public int amount_old_data = 0;
    public int amount_old_cellen_data = 0;
    public int Temp_Sensors_Data = 0;
    public float NomCap_Par = 0.0f;
    public int Mosfets = 0;
    public byte Reg_Design_capacity = 16;
    public static final String EXTRA_VOLTS = "com.example.bluetooth.le.EXTRA_VOLTS";
    public static final String EXTRA_PERCENTAGE = "com.example.bluetooth.le.EXTRA_PERCENTAGE";

    public static final String EXTRA_RAMPS = "com.example.bluetooth.le.EXTRA_RAMPS";

    public static final String EXTRA_TIME = "com.example.bluetooth.le.EXTRA_TIME";

    public static final String EXTRA_TEMP1 = "com.example.bluetooth.le.EXTRA_TEMP1";

    public float Design_capacity = 0.0f;
    public byte Reg_Circulation_capacity = 17;
    public float Circulation_capacity = 0.0f;
    public byte Reg_Single_unit_full_voltage = 18;
    public float Single_unit_full_voltage = 0.0f;
    public byte Reg_Single_cut_off_voltage = 18;
    public float Single_cut_off_voltage = 0.0f;
    public byte Reg_Cell_Under_voltage_par = 38;
    public float Cell_Under_voltage_par = 0.0f;
    public byte Reg_Cell_Under_voltage_releas_par = 39;
    public float Cell_Under_voltage_releas_par = 0.0f;
    public byte Reg_Pack_Under_voltage_par = 34;
    public float Pack_Under_voltage_par = 0.0f;
    public byte Reg_Pack_Under_voltage_releas_par = 35;
    public float Pack_Under_voltage_releas_par = 0.0f;
    float cel1 = 0.0f;
    float cel2 = 0.0f;
    float cel3 = 0.0f;
    float cel4 = 0.0f;
    float cel5 = 0.0f;
    float cel6 = 0.0f;
    float cel7 = 0.0f;
    float cel8 = 0.0f;
    float cel9 = 0.0f;
    float cel10 = 0.0f;
    float cel11 = 0.0f;
    float cel12 = 0.0f;
    float cel13 = 0.0f;
    float cel14 = 0.0f;
    float cel15 = 0.0f;
    float cel16 = 0.0f;
    public boolean ReadComand_ready = false;
    public boolean CellenCommand_ready = false;
    public boolean DataReady = false;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.Andriod.ER.com.BluetoothLeService.1


        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 != 2) {
                if (i2 == 0) {
                    BluetoothLeService.this.mConnectionState = 0;
                    Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                    BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_DISCONNECTED);
                    return;
                }
                return;
            }
            BluetoothLeService.this.mConnectionState = 2;
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_CONNECTED);
            Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return;
            }
            Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                // 1. Get the service and characteristic:
                BluetoothGattService batteryService = bluetoothGatt.getService(YOUR_BATTERY_SERVICE_UUID); // Replace with your actual UUID
                if (batteryService != null) {
                    BluetoothGattCharacteristic batteryLevelCharacteristic = batteryService.getCharacteristic(YOUR_BATTERY_LEVEL_CHARACTERISTIC_UUID); // Replace
                    if (batteryLevelCharacteristic != null) {
                        // 2. Read the characteristic:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                                        != PackageManager.PERMISSION_GRANTED) {
                            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; cannot read characteristic.");
                            return;
                        }
                        mBluetoothGatt.readCharacteristic(batteryLevelCharacteristic);

                        // 3.  Enable notifications if you want to receive automatic updates:
                        mBluetoothGatt.setCharacteristicNotification(batteryLevelCharacteristic, true);

                        // *** ADD THIS DESCRIPTOR CODE ***
                        BluetoothGattDescriptor descriptor = batteryLevelCharacteristic.getDescriptor(
                                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")); // Standard descriptor UUID
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                                            != PackageManager.PERMISSION_GRANTED) {
                                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; cannot write descriptor.");
                                return;
                            }
                            mBluetoothGatt.writeDescriptor(descriptor);
                        } else {
                            Log.w(TAG, "CCCD descriptor not found!");
                        }
                        // *** END DESCRIPTOR CODE ***

                    } else {
                        Log.w(TAG, "Battery level characteristic not found");
                    }
                } else {
                    Log.w(TAG, "Battery service not found");
                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status); // Use status, not i
                Log.i(TAG, "gatt:" + bluetoothGatt); // Log the gatt object
            }
        }



        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
                Log.i(BluetoothLeService.TAG, "Charuuid:" + bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
            Log.i(BluetoothLeService.TAG, "CharChangeduuid:" + bluetoothGattCharacteristic);
        }
    };
    private final IBinder mBinder = new LocalBinder();

    public int getBit(int i, int i2) {
        return (i >> i2) & 1;
    }

    public class oldData {
        public float sec = 0.0f;
        public float min = 0.0f;
        public float hour = 0.0f;
        public float day = 0.0f;
        public float month = 0.0f;
        public float year = 0.0f;
        public float volts = 0.0f;
        public float amps = 0.0f;
        public float Ramps = 0.0f;
        public float percentage = 0.0f;
        public float temp1 = 0.0f;
        public float temp2 = 0.0f;
        public float time = 0.0f;
        public float RemainCap = 0.0f;
        public float NomCap = 0.0f;

        public oldData() {
        }
    }

    /* renamed from: com.Andriod.ER.com.BluetoothLeService$1 */
    class AnonymousClass1 extends BluetoothGattCallback {
        AnonymousClass1() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 != 2) {
                if (i2 == 0) {
                    BluetoothLeService.this.mConnectionState = 0;
                    Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                    BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_DISCONNECTED);
                    return;
                }
                return;
            }
            BluetoothLeService.this.mConnectionState = 2;
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_CONNECTED);
            Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return;
            }
            Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
                return;
            }
            Log.w(BluetoothLeService.TAG, "onServicesDiscovered received: " + i);
            Log.i(BluetoothLeService.TAG, "uuid:" + bluetoothGatt);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
                Log.i(BluetoothLeService.TAG, "Charuuid:" + bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
            Log.i(BluetoothLeService.TAG, "CharChangeduuid:" + bluetoothGattCharacteristic);
        }
    }

    public void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
    }

    public void broadcastUpdate(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Intent intent = new Intent(str);
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (value != null && value.length > 0) {
            StringBuilder sb = new StringBuilder(value.length);
            for (byte b : value) {
                sb.append(String.format("%02X ", Byte.valueOf(b)));
            }
            Log.d(TAG, "Raw data received: " + sb.toString());
            ReadingData(value); // This method updates your class members (volts, percentage, etc.)

            // Now, put the processed data as extras in the intent
            if (ACTION_DATA_AVAILABLE.equals(str) && ReadComand_ready) {
                intent.putExtra(EXTRA_VOLTS, volts);
                intent.putExtra(EXTRA_PERCENTAGE, percentage);
                intent.putExtra(EXTRA_RAMPS, Ramps);
                intent.putExtra(EXTRA_TIME, time);
                intent.putExtra(EXTRA_TEMP1, temp1);
                // Add other relevant data as extras
            }
        }
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public void ReadingData(byte[] bArr) {
        if ((bArr[0] & 255) == 221) {
            this.one = bArr;
            this.combined = bArr;
        } else {
            this.two = bArr;
            byte[] bArr2 = this.one;
            byte[] bArr3 = new byte[bArr2.length + bArr.length];
            System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
            byte[] bArr4 = this.two;
            System.arraycopy(bArr4, 0, bArr3, this.one.length, bArr4.length);
            this.combined = bArr3;
        }
        encodeHexString(this.combined);
        Log.i(TAG, "End of line.. Now DataProcessing");
        procesdata(this.combined);
    }

    public String encodeHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(String.format("%02X ", Byte.valueOf(b)));
        }
        Log.i(TAG, "Data hexString in:" + ((Object) stringBuffer));
        return stringBuffer.toString();
    }

    public void procesdata(byte[] bArr) {
        if ((bArr[1] & 255) == 3 && bArr.length > 30) {
            Log.i(TAG, "DataIn[1] & 0xFF) == 0x03");
            this.volts = (((bArr[4] & 255) << 8) | (bArr[5] & 255)) / 100.0f;
            float f = ((bArr[6] & 255) << 8) | (bArr[7] & 255);
            this.amps = f;
            if (f > 25000.0f) {
                this.Ramps = (f - 65536.0f) / 100.0f;
            } else if (f < 25000.0f) {
                this.Ramps = f / 100.0f;
            }
            float f2 = (((bArr[8] & 255) << 8) | (bArr[9] & 255)) / 100.0f;
            this.RemainCap = f2;
            float f3 = ((bArr[11] & 255) | ((bArr[10] & 255) << 8)) / 100.0f;
            this.NomCap = f3;
            this.percentage = bArr[23];
            float f4 = this.Ramps;
            if (f4 != 0.0d) {
                if (f4 < 0.0d) {
                    this.time = f2 / Math.abs(f4);
                } else if (f4 > 0.0d) {
                    this.time = (f3 - f2) / Math.abs(f4);
                }
            } else if (this.amps == 0.0d) {
                this.time = 0.0f;
            }
            this.temp1 = ((((bArr[27] & 255) << 8) | (bArr[28] & 255)) - 2731.0f) / 10.0f;
            if ((bArr[26] & 255) == 2) {
                this.temp2 = ((((bArr[29] & 255) << 8) | (bArr[30] & 255)) - 2731.0f) / 10.0f;
            } else if ((bArr[26] & 255) == 1) {
                this.temp2 = 0.0f;
            }
            int i = ((bArr[20] & 255) << 8) | (bArr[21] & 255);
            this.lockData = i >> 12;
            this.SOV = getBit(i, 0);
            this.SUV = getBit(i, 1);
            this.POV = getBit(i, 2);
            this.PUV = getBit(i, 3);
            this.COT = getBit(i, 4);
            this.CUT = getBit(i, 5);
            this.DOT = getBit(i, 6);
            this.DUT = getBit(i, 7);
            this.COC = getBit(i, 7);
            this.DOC = getBit(i, 9);
            this.SC = getBit(i, 10);
            this.IC_Error = getBit(i, 11);
            this.Mosfets = bArr[24];
            this.DataReady = true;
            this.ReadComand_ready = true;
            return;
        }
        if ((bArr[1] & 255) == 4) {
            int i2 = (bArr[3] & 255) / 2;
            this.AantalCellen = i2;
            process_Cells(bArr, i2);
            this.cellenDataReady = true;
            return;
        }
        if ((bArr[1] & 255) == 46) {
            this.Temp_Sensors_Data = bArr[5] & 255;
            return;
        }
        if ((bArr[1] & 255) == 16) {
            this.NomCap_Par = ((bArr[5] & 255) | ((bArr[4] & 255) << 8)) / 100.0f;
            return;
        }
        if ((bArr[1] & 255) == this.Reg_Cell_Under_voltage_releas_par) {
            if ((bArr[2] & 255) == 0 && (bArr[2] & 255) == 80) {
                return;
            }
            this.Cell_Under_voltage_releas_par = ((bArr[5] & 255) | ((bArr[4] & 255) << 8)) / 1000.0f;
            return;
        }
        if ((bArr[1] & 255) == this.Reg_Cell_Under_voltage_par) {
            if ((bArr[2] & 255) == 0 && (bArr[2] & 255) == 80) {
                return;
            }
            this.Cell_Under_voltage_par = ((bArr[5] & 255) | ((bArr[4] & 255) << 8)) / 1000.0f;
            return;
        }
        if ((bArr[1] & 255) == this.Reg_Pack_Under_voltage_releas_par) {
            if ((bArr[2] & 255) == 0 && (bArr[2] & 255) == 80) {
                return;
            }
            this.Pack_Under_voltage_releas_par = ((bArr[5] & 255) | ((bArr[4] & 255) << 8)) / 100.0f;
            return;
        }
        if ((bArr[1] & 255) == this.Reg_Pack_Under_voltage_par) {
            if ((bArr[2] & 255) == 0 && (bArr[2] & 255) == 80) {
                return;
            }
            this.Pack_Under_voltage_par = ((bArr[5] & 255) | ((bArr[4] & 255) << 8)) / 100.0f;
            return;
        }
        Log.i(TAG, "Unexpected Data:" + bArr);
    }

    public void process_Cells(byte[] bArr, int i) {
        if (i >= 4) {
            this.cel1 = (((bArr[4] & 255) << 8) | (bArr[5] & 255)) / 1000.0f;
            this.cel2 = (((bArr[6] & 255) << 8) | (bArr[7] & 255)) / 1000.0f;
            this.cel3 = (((bArr[8] & 255) << 8) | (bArr[9] & 255)) / 1000.0f;
            this.cel4 = (((bArr[10] & 255) << 8) | (bArr[11] & 255)) / 1000.0f;
        }
        if (i >= 8) {
            this.cel5 = (((bArr[12] & 255) << 8) | (bArr[13] & 255)) / 1000.0f;
            this.cel6 = (((bArr[14] & 255) << 8) | (bArr[15] & 255)) / 1000.0f;
            this.cel7 = (((bArr[16] & 255) << 8) | (bArr[17] & 255)) / 1000.0f;
            this.cel8 = (((bArr[18] & 255) << 8) | (bArr[19] & 255)) / 1000.0f;
        }
        if (i >= 12) {
            this.cel9 = (((bArr[20] & 255) << 8) | (bArr[21] & 255)) / 1000.0f;
            this.cel10 = (((bArr[22] & 255) << 8) | (bArr[23] & 255)) / 1000.0f;
            this.cel11 = (((bArr[24] & 255) << 8) | (bArr[25] & 255)) / 1000.0f;
            this.cel12 = (((bArr[26] & 255) << 8) | (bArr[27] & 255)) / 1000.0f;
        }
        if (i >= 16) {
            this.cel13 = (((bArr[28] & 255) << 8) | (bArr[29] & 255)) / 1000.0f;
            this.cel14 = (((bArr[30] & 255) << 8) | (bArr[31] & 255)) / 1000.0f;
            this.cel15 = (((bArr[32] & 255) << 8) | (bArr[33] & 255)) / 1000.0f;
            this.cel16 = ((bArr[35] & 255) | ((bArr[34] & 255) << 8)) / 1000.0f;
        }
        this.CellenCommand_ready = true;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            this.mBluetoothManager = bluetoothManager;
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        BluetoothAdapter adapter = this.mBluetoothManager.getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String str) {
        if (this.mBluetoothAdapter == null || str == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        String str2 = this.mBluetoothDeviceAddress;
        Log.d(TAG, "new Address."+str);
        Log.d(TAG, "new Address2."+str);
        if (str2 != null && str.equals(str2) && this.mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return false;
            }
            if (this.mBluetoothGatt.connect()) {
                Log.w(TAG, "Connection happens.");
                this.mConnectionState = 1;
                return true;
            }

            this.mBluetoothGatt = this.mBluetoothAdapter.getRemoteDevice(str).connectGatt(this, false, this.mGattCallback);
            this.mBluetoothDeviceAddress = str;
            return false;
        }
        BluetoothDevice remoteDevice = this.mBluetoothAdapter.getRemoteDevice(str);
        if (remoteDevice == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        this.mBluetoothGatt = remoteDevice.connectGatt(this, false, this.mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        this.mBluetoothDeviceAddress = str;
        Log.d(TAG, "new Address3."+remoteDevice);
        this.mConnectionState = 1;
        return true;
    }

    public void disconnect() {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return;
            }
            bluetoothGatt.disconnect();
        }
    }

    public void close() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        bluetoothGatt.close();
        this.mBluetoothGatt = null;
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
            // Optional: notify the Activity to recheck permissions
            return;
        }
        bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
        Log.i(TAG, "characteristic" + bluetoothGattCharacteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return;
            }
            bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(BluetoothLeService.this, Manifest.permission.BLUETOOTH_CONNECT)
                            != PackageManager.PERMISSION_GRANTED) {

                Log.w(TAG, "Missing BLUETOOTH_CONNECT permission; skipping service discovery.");
                // Optional: notify the Activity to recheck permissions
                return;
            }
            bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return null;
        }
        return bluetoothGatt.getServices();
    }
}
