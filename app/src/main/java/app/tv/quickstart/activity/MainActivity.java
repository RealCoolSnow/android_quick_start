package app.tv.quickstart.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import app.tv.quickstart.R;
import app.tv.quickstart.base.BaseActivity;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
