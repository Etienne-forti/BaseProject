package com.artemkopan.baseproject.dialog;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.artemkopan.baseproject.R;

/**
 * Created by Artem Kopan for BaseProject
 * 09.10.2016
 */

@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
public class DialogProvider {

    private ProgressDialog progressDialog;
    private MessageDialog messageDialog;

    //==============================================================================================
    // Progress Dialog
    //==============================================================================================
    //region methods
    public synchronized ProgressDialog showProgress(Object obj) {
        return showProgress(obj, R.string.base_info_loading);
    }

    public synchronized ProgressDialog showProgress(Object obj, @StringRes int description) {
        if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            return this.showProgress(fragment,
                                     fragment.getString(description));
        } else if (obj instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) obj;
            return this.showProgress(activity,
                                     activity.getString(description));
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    public synchronized ProgressDialog showProgress(Object obj, String description) {
        if (progressDialog == null || progressDialog.isShowing()) {
            if (progressDialog != null) progressDialog.dismiss();
            progressDialog = ProgressDialog.newInstance(description);
            progressDialog.show(getFragmentManager(obj));
        } else {
            progressDialog.setDescription(description);
        }
        progressDialog.setCancelable(false);
        return progressDialog;

    }

    public synchronized void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismissAllowingStateLoss();
            progressDialog = null;
        }
    }
    //endregion

    //==============================================================================================
    // Message Dialog
    //==============================================================================================
    //region methods
    public synchronized MessageDialog showMessage(Object obj, @StringRes int description) {
        return showMessage(obj, R.string.base_info_something_wrong, description);
    }

    public synchronized MessageDialog showMessage(Object obj, String description) {
        String title;
        if (obj instanceof Fragment) {
            title = ((Fragment) obj).getString(R.string.base_info_something_wrong);
        } else if (obj instanceof FragmentActivity) {
            title = ((FragmentActivity) obj).getString(R.string.base_info_something_wrong);
        } else {
            title = null;
        }

        return showMessage(obj, title, description);
    }

    public synchronized MessageDialog showMessage(Object obj, @StringRes int title, @StringRes int description) {
        if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            return this.showMessage(fragment,
                                    fragment.getString(title),
                                    fragment.getString(description));
        } else if (obj instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) obj;
            return this.showMessage(activity,
                                    activity.getString(title),
                                    activity.getString(description));
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public synchronized MessageDialog showMessage(Object obj, final String title, final String description) {
        if (messageDialog == null || messageDialog.isShowing()) {
            if (messageDialog != null) messageDialog.dismiss();
            messageDialog = MessageDialog.newInstance(title, description);
            messageDialog.show(getFragmentManager(obj));
        } else {
            messageDialog.setTitle(title);
            messageDialog.setDescription(description);
        }
        return messageDialog;
    }

    public synchronized void dismissMessage() {
        if (messageDialog != null) {
            messageDialog.dismissAllowingStateLoss();
            messageDialog = null;
        }
    }
    //endregion

    private static FragmentManager getFragmentManager(Object obj) {
        FragmentManager fragmentManager;
        if (obj instanceof Fragment) {
            fragmentManager = ((Fragment) obj).getFragmentManager();
        } else if (obj instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) obj).getSupportFragmentManager();
        } else {
            throw new IllegalArgumentException("obj must be instance of Fragment or FragmentActivity");
        }
        return fragmentManager;
    }
}
