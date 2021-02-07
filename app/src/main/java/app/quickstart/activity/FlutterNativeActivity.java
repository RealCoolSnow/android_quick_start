package app.quickstart.activity;

import app.quickstart.base.BaseActivity;
import app.quickstart.databinding.ActivityFlutterNativeBinding;

/**
 * File: FlutterNativeActivity
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2021/2/7 17:36
 * Description:
 */
public class FlutterNativeActivity extends BaseActivity<ActivityFlutterNativeBinding> {

    @Override
    protected void initView() {
        binding.btnFinish.setOnClickListener(v -> finish());
    }
}
