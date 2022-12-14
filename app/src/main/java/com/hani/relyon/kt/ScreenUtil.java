package com.hani.relyon.kt;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author : DS-L
 * @date : 2020/6/9
 *  
 * description :
 */
public class ScreenUtil {

    private static int WIDTH;
    private static int HEIGHT;
    private static float DENSITY;

    public static int getWidth(Context context) {
        if (WIDTH == 0) {
            WIDTH = context.getResources().getDisplayMetrics().widthPixels;
        }
        return WIDTH;
    }

    public static int getHeight(Context context) {
        if (HEIGHT == 0) {
            HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        }
        return HEIGHT;
    }

    public static float getDensity(Context context) {
        if (DENSITY == 0) {
            DENSITY = context.getResources().getDisplayMetrics().density;
        }
        return DENSITY;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getDensity(context) + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() !=
                PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable?????????bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // ????????????????????????Matrix??????
        Matrix matrix = new Matrix();
        // ??????????????????
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // ??????????????????
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    public static Drawable zoomDrawableByScale(Drawable drawable, float scale_x, float scale_y) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(scale_x, scale_y);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * ??????????????????????????????"com.vincent"
     */
    public static String getPackageName(Context context) {
        try {
            // ??????packagemanager?????????
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()???????????????????????????0???????????????????????????
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * ?????????????????????????????????"1.0"
     */
    public static String getVersionName(Context context) {
        try {
            // ??????packagemanager?????????
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()???????????????????????????0???????????????????????????
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ?????????????????????????????????1
     */
    public static int getVersionCode(Context context) {
        try {
            // ??????packagemanager?????????
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()???????????????????????????0???????????????????????????
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * ??????????????????????????????
     */

    public static int getOneThirdsWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mWidth = dm.widthPixels;
        return mWidth / 3;
    }

    /**
     * ??????????????????????????????
     */
    public static int getTwoThirdsWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mWidth = dm.widthPixels;
        return mWidth * 2 / 3;
    }

    /**
     * ??????????????????????????????
     */
    public static int getFourFifthsWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mWidth = dm.widthPixels;
        return mWidth * 4 / 5;
    }

    /**
     * ??????????????????????????????
     */
    public static int getOneSecondsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight / 2;
    }

    /**
     * ??????????????????????????????
     */
    public static int getOneThirdsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight / 3;
    }

    /**
     * ??????????????????????????????
     */
    public static int getOneFourthsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight / 4;
    }

    /**
     * ??????????????????????????????
     */
    public static int getTwoThirdsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight * 2 / 3;
    }

    /**
     * ??????????????????????????????
     */
    public static int getTwoFifthsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight * 2 / 5;
    }

    /**
     * ??????????????????????????????
     */
    public static int getFourFifthsHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int mHeight = dm.heightPixels;
        return mHeight * 4 / 5;
    }

    /**
     * @param activity
     * @param permission
     * @return ???????????????????????????
     */
    private static String getAuthorityFromPermission(Activity activity, String permission) {
        if (permission == null) {
            return null;
        }
        List<PackageInfo> packs = activity.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)) {
                            return provider.authority;
                        }
                        if (permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasShortCut(Activity activity, int appNameResId) {
        final String uriStr = "content://" + getAuthorityFromPermission(activity, "com.android.launcher.permission.READ_SETTINGS") + "/favorites?notify=true";
        final Cursor cursor = activity.getContentResolver().query(Uri.parse(uriStr), new String[]{"title", "iconResource"}, "title=?", new String[]{activity.getString(appNameResId)}, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            return true;
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return false;
        }
    }

    public static void hideSoftInput(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0); //??????????????????
        }
    }
    public static void showSoftInput(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public static boolean hasNotchScreen(Activity activity) {
        if (getInt("ro.miui.notch", activity) == 1 || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
                || hasNotchAtVivo(activity) || isAndroidP(activity)) { //TODO ????????????
            return true;
        }

        return false;
    }

    /**
     * Android P ???????????????
     *
     * @param activity
     * @return
     */
    public static boolean isAndroidP(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null && android.os.Build.VERSION.SDK_INT >= 28) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null)
                return true;
        }
        return false;
    }

    /**
     * ?????????????????????.
     *
     * @return 0 if it is not notch ; return 1 means notch
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key, Activity activity) {
        int result = 0;

        try {
            ClassLoader classLoader = activity.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            //????????????
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            //??????
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Integer(0);
            result = (Integer) getInt.invoke(SystemProperties, params);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Huawei", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Huawei", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Huawei", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static final int VIVO_NOTCH = 0x00000020;//???????????????
    public static final int VIVO_FILLET = 0x00000008;//???????????????

    /**
     * VIVO???????????????
     *
     * @return
     */
    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Vivo", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Vivo", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Vivo", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    /**
     * OPPO???????????????
     *
     * @return
     */
    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    public static String getDeviceBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getDeviceSdkVersion() {
        return System.getProperty("ro.build.version.sdk");
    }

    public static String getDeviceAbi() {
        return System.getProperty("ro.product.cpu.abi");
    }

    public static String[] getDeviceAbis() {
        String property = System.getProperty("ro.product.cpu.abilist");
        if (TextUtils.isEmpty(property)) {
            return null;
        }
        return property.split(",");
    }

    public static String getOsDescStr() {
        return System.getProperty("ro.build.version.base_os");
    }


    public static int getHeight(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
