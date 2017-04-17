package com.artemkopan.base_mvp.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.artemkopan.base_mvp.fragment.BaseFragment;
import com.artemkopan.base_mvp.presentation.Presentation;
import com.artemkopan.base_mvp.presentation.PresentationManager;
import com.artemkopan.base_mvp.presenter.BasePresenter;
import com.artemkopan.base_mvp.view.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView>
        extends AppCompatActivity
        implements BaseView, Presentation {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressWarnings("SpellCheckingInspection")
    protected P presenter;
    protected PresentationManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        manager = PresentationManager.Factory.create(this);
        super.onCreate(savedInstanceState);
        manager.onCreate();
        manager.onCreateView(null, null, this, null);
    }

    @SuppressWarnings("unused, unchecked")
    public void injectPresenter(P presenter, boolean attach) {
        this.presenter = presenter;
        if (attach) this.presenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
        }
        manager.onDestroyView();
        manager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount() - 1;
        if (backStackCount >= 0) {
            BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount);
            String str = backEntry.getName();

            Fragment fragment = fragmentManager.findFragmentByTag(str);

            if (fragment != null && fragment instanceof BaseFragment && !((BaseFragment) fragment).onBackPressed()) {
                return;
            } else {
                getSupportFragmentManager().popBackStackImmediate();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void showError(@Nullable Object tag, String error) {

    }

    @Override
    public void showProgress(@Nullable Object tag) {

    }

    @Override
    public void hideProgress(@Nullable Object tag) {

    }

    @Override
    public FragmentActivity getBaseActivity() {
        return this;
    }

    @Override
    public CompositeDisposable getOnDestroyDisposable() {
        return manager.getOnDestroyDisposable();
    }

    @Override
    @Nullable
    public View getView() {
        return findViewById(android.R.id.content);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId) {
        return manager.onToolbarInit(toolbarId, true);
    }

    @Nullable
    public Toolbar onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        return manager.onToolbarInit(toolbarId, homeDrawable, true);
    }

    public void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        manager.onToolbarNavigationClickListener(onClickListener);
    }

    public void onToolbarSetTitle(@StringRes int titleRes) {
        manager.onToolbarSetTitle(titleRes);
    }

    public void onToolbarSetTitle(CharSequence title) {
        manager.onToolbarSetTitle(title);
    }

    public void setStatusBarColor(@ColorInt int color) {
        manager.setStatusBarColor(color);
    }

    public void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        manager.setStatusBarColor(color, delay, timeUnit);
    }
}