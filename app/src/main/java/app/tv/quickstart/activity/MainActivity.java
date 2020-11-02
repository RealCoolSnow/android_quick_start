package app.tv.quickstart.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import app.tv.network.BaseObserver;
import app.tv.network.RxScheduler;
import app.tv.network.bean.BaseResp;
import app.tv.quickstart.R;
import app.tv.quickstart.api.bean.req.HelloReq;
import app.tv.quickstart.api.bean.resp.HelloResp;
import app.tv.quickstart.base.BaseActivity;
import app.tv.quickstart.network.RetrofitFactory;
import io.reactivex.Observable;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_app_list).setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, AppInstalledActivity.class));
        });
        findViewById(R.id.btn_http_test).setOnClickListener((v) -> {
            httpTest();
        });
    }

    private void httpTest() {
        HelloReq helloReq = new HelloReq();
        Observable<BaseResp<HelloResp>> observable = RetrofitFactory.getInstance().hello(helloReq.toRequestBody());
        observable.compose(RxScheduler.compose(this.bindToLifecycle())).subscribe(new BaseObserver<HelloResp>(this) {
            @Override
            protected void onSuccess(String msg, HelloResp data) {
            }

            @Override
            protected void onError(int code, String msg) {
            }
        });
    }
}
