package com.artemkopan.baseproject.sample;

import android.os.Bundle;

import com.artemkopan.baseproject.activity.BaseActivity;
import com.artemkopan.baseproject.dialog.DialogProvider;
import com.artemkopan.baseproject.helper.Log;
import com.artemkopan.baseproject.recycler.view.ExRecyclerView;
import com.artemkopan.baseproject.sample.ui.activity.Test2Activity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.disposables.CompositeDisposable;

public class Test extends BaseActivity {

    DialogProvider mDialogProvider = new DialogProvider();

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ExRecyclerView list = (ExRecyclerView) findViewById(R.id.list);

        list.showProgress();

        mDialogProvider.showProgressDialog(this, new Runnable() {
            @Override
            public void run() {
                Test2Activity.startActivity(Test.this);
            }
        });

        mCompositeDisposable.add(
                testObservable("Composite")
                        .takeUntil(mLifecycleSubject).subscribe());

        testObservable("Tale Until")
                .takeUntil(mLifecycleSubject).subscribe();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private Observable<Object> testObservable(final String test) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                e.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        Log.i("onDispose: " + test);
                    }
                });
            }
        });
    }
}
