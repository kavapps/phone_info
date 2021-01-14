package com.kavapps.phoneinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


public class PhoneInfo {

    private static ActivityManager.MemoryInfo memoryInfo;
    private ActivityManager activityManager;
    private TelephonyManager telephonyManager;
    private static DisplayMetrics metrics;
    private IntentFilter intentFilter;
    private DisplayInfo displayInfo;
    private RamInfo ramInfo;
    private SimInfo simInfo;
    private BatteryInfo batteryInfo;
    private BaseInfo baseInfo;
    private ProcessorInfo processorInfo;
    private Intent intent;


    public PhoneInfo(Activity activity) {
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intent = activity.registerReceiver(null, intentFilter);

        displayInfo = new DisplayInfo();
        ramInfo = new RamInfo();
        simInfo = new SimInfo();
        batteryInfo = new BatteryInfo(intent,activity);
        baseInfo = new BaseInfo();
        processorInfo = new ProcessorInfo();
    }

    public ProcessorInfo getProcessorInfo() {
        return processorInfo;
    }

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public DisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public RamInfo getRamInfo() {
        return ramInfo;
    }

    public SimInfo getSimInfo() {
        return simInfo;
    }

    public BatteryInfo getBatteryInfo() {
        return batteryInfo;
    }

    public static class BaseInfo {
        public String getBrand() {
            return Build.BRAND;
        }

        public String getManufacturer() {
            return Build.MANUFACTURER;
        }

        public String getModel() {
            return Build.MODEL;
        }

        public String getAndroidVersion() {
            return Build.VERSION.RELEASE;
        }

        public String getIncrementalVersion() {
            return Build.VERSION.INCREMENTAL;
        }

        public String getSdkVersion() {
            return String.valueOf(Build.VERSION.SDK_INT);
        }
    }

    public static class RamInfo {

        public String getFreeRam() {
            return String.valueOf(memoryInfo.availMem);
        }

        public String getTotalRam() {
            return String.valueOf(memoryInfo.totalMem);
        }
    }

    public static class DisplayInfo {

        public String getHeightPixels() {
            return String.valueOf(metrics.heightPixels);
        }

        public String getWidthPixelsPixels() {
            return String.valueOf(metrics.widthPixels);
        }

        public String getXdpi() {
            return String.valueOf(metrics.xdpi);
        }

        public String getYdpi() {
            return String.valueOf(metrics.ydpi);
        }

        public String getDensityDpi() {
            return String.valueOf(metrics.densityDpi);
        }

        public String getScaledDensity() {
            return String.valueOf(metrics.scaledDensity);
        }

    }

    public static class BatteryInfo {

        Intent intent;
        BatteryManager batteryManager;

        public BatteryInfo(Intent intent, Activity activity) {
            this.intent = intent;
             batteryManager = (BatteryManager) activity.getSystemService(Context.BATTERY_SERVICE);

        }

        public String getHealth() {
            return String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0));
        }

        public String getChargingInPercent() {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = 0;
            if (level != -1 && scale != -1) {
                batteryPct = (int) ((level / (float) scale) * 100f);
            }

            return String.valueOf(batteryPct);
        }

        public String getStatus() {
            return String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
        }

        public boolean isСharging() {
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            return plugged != 0;
        }

        public String getTemperature(){
            return String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0));
        }

        public String getTechnology(){
            String technology = "";
            if (intent.getExtras() != null) {
                technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            }
            return technology;
        }

        public String getVoltage(){
            return String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
        }

        public String getCapacity(){
            return String.valueOf(getBatteryCapacity());
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private long getBatteryCapacity() {
            if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.LOLLIPOP) {
                Long chargeCounter = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
                Long capacity = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                if (chargeCounter != null && capacity != null) {
                    long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                    return value;
                }
            }

            return 0;
        }


    }

    public class SimInfo {

        public String getCallState(){
            return String.valueOf(telephonyManager.getCallState());
        }

        public String getPhoneType(){
            return String.valueOf(telephonyManager.getPhoneType());
        }

        public String getNetworkCountryIso(){
            return String.valueOf(telephonyManager.getNetworkCountryIso());
        }

        public String getNetworkOperatorName(){
            return String.valueOf(telephonyManager.getNetworkOperatorName());
        }

        public String getDataState(){
            return String.valueOf(telephonyManager.getDataState());
        }

        public String getDataActivity(){
            return String.valueOf(telephonyManager.getCallState());
        }

        public String isNetworkRoaming(){
            return String.valueOf((telephonyManager.isNetworkRoaming() ? "true" : "false"));
        }

        public String getSimState(){
            return String.valueOf(telephonyManager.getSimState());
        }

        public String getSimCountryIso(){
            return String.valueOf(telephonyManager.getSimCountryIso());
        }

        public String getSimOperator(){
            return String.valueOf(telephonyManager.getSimOperator());
        }

        public String getSimOperatorName(){
            return String.valueOf(telephonyManager.getSimOperatorName());
        }
    }

    public class ProcessorInfo{

        public String getName(){
            return Build.HARDWARE;
        }

        public String getCountKernel(){
            return String.valueOf(Runtime.getRuntime().availableProcessors());
        }
    }


}
