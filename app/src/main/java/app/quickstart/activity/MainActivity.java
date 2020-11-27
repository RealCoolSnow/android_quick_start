package app.quickstart.activity;

import android.widget.Toast;

import app.quickstart.api.bean.req.HelloReq;
import app.quickstart.api.bean.resp.HelloResp;
import app.quickstart.base.BaseActivity;
import app.quickstart.databinding.ActivityMainBinding;
import app.quickstart.network.RetrofitFactory;
import app.network.BaseObserver;
import app.network.RxScheduler;
import app.network.bean.BaseResp;
import io.reactivex.Observable;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void initView() {
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
