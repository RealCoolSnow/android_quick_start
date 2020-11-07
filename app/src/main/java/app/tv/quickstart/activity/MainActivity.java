package app.tv.quickstart.activity;

import android.content.Intent;
import android.widget.Toast;

import app.tv.network.BaseObserver;
import app.tv.network.RxScheduler;
import app.tv.network.bean.BaseResp;
import app.tv.quickstart.api.bean.req.HelloReq;
import app.tv.quickstart.api.bean.resp.HelloResp;
import app.tv.quickstart.base.BaseActivity;
import app.tv.quickstart.databinding.ActivityMainBinding;
import app.tv.quickstart.network.RetrofitFactory;
import io.reactivex.Observable;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void initView() {
        binding.btnAppList.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AppInstalledActivity.class));
        });
        binding.btnHttpTest.setOnClickListener(view -> httpTest());
    }

    private void httpTest() {
        HelloReq helloReq = new HelloReq();
        Observable<BaseResp<HelloResp>> observable = RetrofitFactory.getInstance().hello(helloReq.toRequestBody());
        observable.compose(RxScheduler.compose(this.bindToLifecycle())).subscribe(new BaseObserver<HelloResp>(this) {
            @Override
            protected void onSuccess(String msg, HelloResp data) {
                Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onError(int code, String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
