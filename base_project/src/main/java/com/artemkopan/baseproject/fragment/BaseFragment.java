package com.artemkopan.baseproject.fragment;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.presenter.BasePresenter;
import com.artemkopan.baseproject.presenter.MvpView;
import com.artemkopan.baseproject.rx.BaseRx;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public abstract class BaseFragment<P extends BasePresenter<V>, V extends MvpView> extends Fragment
        implements MvpView {

    public PublishSubject<Object> mDestroySubject = PublishSubject.create();

    @Nullable
    protected Toolbar mToolbar;
    protected P mPresenter;
    @SuppressWarnings("SpellCheckingInspection")
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (onCreateInflateView() > 0) {
            View view = inflater.inflate(onCreateInflateView(), container, false);
            mUnbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDestroySubject.onNext(BaseRx.TRIGGER);
        super.onDestroy();
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


    //region Toolbar methods

    /**
     * Toolbar init. Usually call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param fromActivity If need find toolbar in {@link AppCompatActivity}
     */
    protected void onToolbarInit(@IdRes int toolbarId, boolean fromActivity) {
        onToolbarInit(toolbarId, 0, fromActivity);
    }

    /**
     * Toolbar init. Usually call {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param toolbarId    Res id your toolbar;
     * @param homeDrawable Set home image resources ( - optional)
     * @param fromActivity If need find toolbar in {@link AppCompatActivity}
     */
    protected void onToolbarInit(@IdRes int toolbarId, @DrawableRes int homeDrawable,
            boolean fromActivity) {

        if (fromActivity && getActivity() != null) {
            mToolbar = findById(getActivity(), toolbarId);
        } else if (!fromActivity && getView() != null) {
            mToolbar = findById(getView(), toolbarId);
        } else {
            Log.e("From Activity: " + fromActivity +
                    "\nActivity: " + getActivity() +
                    "\nView: " + getView());
        }

        if (mToolbar == null) {
            throw new IllegalArgumentException("Can't find toolbar id");
        } else if (homeDrawable > 0) {
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), homeDrawable));
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
     * Click listener for toolbar navigation item; Or you can use {@link #onToolbarHomeBtn(boolean)}
     * P.S. if you use {@link #onToolbarHomeBtn(boolean)} clicked was call in parent activity
     */
    protected void onToolbarNavigationClickListener(OnClickListener onClickListener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(onClickListener);
        }
    }

    /**
     * If you want enable home button. You can listen event in {@link #onOptionsItemSelected(MenuItem)} with item id {@link android.R.id#home}
     */
    protected void onToolbarHomeBtn(boolean show) {
        if (getActivity() != null
                && getActivity() instanceof AppCompatActivity
                && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(show);
        }
    }

    protected void setActionBar(@Nullable Toolbar toolbar) {
        if (toolbar != null && getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    //endregion

    //region status bar methods
    protected void setStatusBarColor(@ColorInt int color) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setStatusBarColor(color);
        } else {
            Log.e("Please check your activity on null or extends " + getActivity());
        }
    }

    protected void setStatusBarColor(@ColorInt int color, long delay, TimeUnit timeUnit) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setStatusBarColor(color, delay, timeUnit);
        } else {
            Log.e("Please check your activity on null or extends " + getActivity());
        }
    }
    //endregion


    /**
     * @return Check implemented class and return them. If fragment was started from another
     * fragment {@link #getTargetFragment()} or {@link #getParentFragment()},
     * then used {@link #getParentFragment()} , else {@link #getActivity()}
     */
    @Nullable
    public <T> T getParentClass(Class<T> clazz) {
        if (getTargetFragment() != null && clazz.isInstance(getTargetFragment())) {
            return clazz.cast(getTargetFragment());
        } else if (getParentFragment() != null && clazz.isInstance(getParentFragment())) {
            return clazz.cast(getParentFragment());
        } else if (getActivity() != null && clazz.isInstance(getActivity())) {
            return clazz.cast(getActivity());
        }
        return null;
    }
}
