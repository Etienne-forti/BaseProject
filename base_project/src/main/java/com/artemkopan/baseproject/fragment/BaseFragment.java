package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.rx.Lifecycle;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();
    @SuppressWarnings("SpellCheckingInspection")
    protected Unbinder mUnbinder;

    @Nullable
    protected ActionBar mActionBar;
    @Nullable
    protected Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (onCreateInflateView() > 0) {
            View view = inflater.inflate(onCreateInflateView(), container, false);
            mUnbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        super.onDestroy();
    }

    public void onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        if (fromActivity && getActivity() != null) {
            mToolbar = findById(getActivity(), toolbarId);
        } else if (fromActivity && getView() != null) {
            mToolbar = findById(getView(), toolbarId);
        } else {
            Log.e(TAG,
                    "From Activity: " + fromActivity +
                            "\nActivity: " + getActivity() +
                            "\nView: " + getView());
        }
        setActionBar(mToolbar);
    }

    public void onToolbarSetTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    public void onToolbarShowBackBtn(boolean show) {
        if (mActionBar == null) {
            return;
        }
        if (show) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setActionBar(Toolbar toolbar) {
        if (toolbar != null && getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
    }

    /**
     * Call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} with auto inflate
     *
     * @return {@link LayoutRes} layout res id
     */
    public abstract int onCreateInflateView();

    /**
     * @return if true = {@link BaseActivity#onBackPressed()} was called;
     */
    public boolean onBackPressed() {
        return true;
    }

}
