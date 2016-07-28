package com.artemkopan.baseproject.helper;

import android.text.TextUtils;


public class Log {

    private static boolean sLog;

    public static void enable(boolean isEnable) {
        sLog = isEnable;
    }

    public static void i(String tag, String string) {
        if (sLog) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (sLog) android.util.Log.e(tag, string);
    }

    public static void e(String tag, String string, Throwable throwable) {
        if (sLog) android.util.Log.e(tag, string, throwable);
    }

    public static void w(String tag, String string) {
        if (sLog) android.util.Log.w(tag, string);
    }

    public static void w(String tag, String string, Throwable throwable) {
        if (sLog) android.util.Log.w(tag, string, throwable);
    }

    public static void d(String tag, String string) {
        if (sLog) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (sLog) android.util.Log.v(tag, string);
    }

    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;

        for (StackTraceElement trace : traces) {
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                }
            } catch (ClassNotFoundException ignored) {

            }
        }

        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

}
