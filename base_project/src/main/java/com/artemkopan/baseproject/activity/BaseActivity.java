package com.artemkopan.baseproject.activity;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.artemkopan.baseproject.fragment.BaseFragment;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.artemkopan.baseproject.rx.Lifecycle;
import com.artemkopan.baseproject.utils.ExtraUtils;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;


public abstract class BaseActivity<P extends BasePresenter<V>, V extends MvpView> extends AppCompatActivity implements MvpView {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public PublishSubject<Lifecycle> mPublishSubject = PublishSubject.create();

    @SuppressWarnings("SpellCheckingInspection")
    protected Unbinder mUnbinder;
    protected Toolbar mToolbar;
    protected P mPresenter;

    public void bindButterKnife() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        mPublishSubject.onNext(Lifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mPublishSubject.onNext(Lifecycle.ON_DESTROY);
        super.onDestroy();
    }

    @Override
    public void showError(@Nullable Object tag, String error) {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount() - 1;
        if (backStackCount >= 0) {
            FragmentManager.BackStackEntry backEntry =
                    fragmentManager.getBackStackEntryAt(backStackCount);
            String str = backEntry.getName();

            Fragment fragment = fragmentManager.findFragmentByTag(str);

            if (fragment != null && fragment instanceof BaseFragment) {
                if (!((BaseFragment) fragment).onBackPressed()) {
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public void setStatusBarColor(@ColorInt int color) {
        if (ExtraUtils.postLollipop()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void setStatusBarColor(@ColorInt final int color, long delay, TimeUnit timeUnit) {
        if (ExtraUtils.postLollipop()) {
            Observable.timer(delay, timeUnit, AndroidSchedulers.mainThread())
                    .takeUntil(mPublishSubject)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            setStatusBarColor(color);
                        }
                    });
        }

    }


    /**
     * Toolbar init. Usually call {@link #onCreate(Bundle)}
     *
     * @param toolbarId Res id your toolbar;
     */
    protected void onToolbarInit(@IdRes int toolbarId) {
        onToolbarInit(toolbarId, 0);
    }

    /**
     * Toolbar init. Usually call {@link #onCreate(Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param homeDrawable Set home image resources ( - optional)
     */
    protected void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable) {
        mToolbar = findById(this, toolbarId);

        if (mToolbar == null) {
            Log.e("onToolbarInit: Can't find toolbar", new IllegalArgumentException());
            return;
        } else if (homeDrawable > 0) {
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, homeDrawable));
        }

        setActionBar(mToolbar);
    }

    /**
     * Set toolbar title
     *
     * @param titleRes string res value
     */
    protected void onToolbarSetTitle(@StringRes int titleRes) {
        onToolbarSetTitle(getString(titleRes));
    }

    /**
     * Set toolbar title
     *
     * @param title title string value
     */
    protected void onToolbarSetTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    /**
     * If you want enable home button. You can listen event in {@link #onOptionsItemSelected(MenuItem)} with item id {@link android.R.id#home}
     */
    protected void onToolbarHomeBtn(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionBar(@Nullable Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    //endregion
}