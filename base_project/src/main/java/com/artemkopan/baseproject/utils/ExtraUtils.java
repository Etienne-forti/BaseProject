package com.artemkopan.baseproject.utils;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ExtraUtils {

    /**
     * Hide keyboard if view !=null
     *
     * @param view current focused view
     */
    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Open keyboard
     *
     * @param view View for request focus; if view == null - nothing happens
     */
    public static void openKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.toggleSoftInputFromWindow(
                view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        view.requestFocus();
    }

    /**
     * Check current internet connection
     * @param context {@link Application#getApplicationContext()}
     * @return if internet (WIFI or MOBILE) is connected return true;
     */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Get current width of display
     *
     * @param context {@link Application#getApplicationContext()}
     * @return current width in pixels
     */
    public static int getWidnowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Get current height of display
     *
     * @param context {@link Application#getApplicationContext()}
     * @return current height in pixels
     */
    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Keep the CPU On! If you want keep the Screen On you must add flag   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) or  android:keepScreenOn="true" in  layout
     *
     * @param context for get power manager service
     * @return wake lock for {@link ExtraUtils#wakeUnlock(PowerManager.WakeLock)}
     */
    public static PowerManager.WakeLock wakeLock(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, context.getClass().getSimpleName());
        wakeLock.acquire();
        return wakeLock;
    }

    /**
     * unlock cpu
     *
     * @param wakeLock acuired wakelock
     */
    public static void wakeUnlock(PowerManager.WakeLock wakeLock) {
        wakeLock.release();
    }


    /**
     * Allow all ssl certificates
     *
     * @return {@link SSLSocketFactory}
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext getUnsafeSSL() throws NoSuchAlgorithmException, KeyManagementException {

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager

        return sslContext;
    }

}
