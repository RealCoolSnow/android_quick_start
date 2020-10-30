package app.tv.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.ItemBridgeAdapter;

import java.util.ArrayList;
import java.util.List;

import app.tv.common.util.FontDisplayUtil;
import app.tv.quickstart.R;
import app.tv.quickstart.base.BaseActivity;
import app.tv.quickstart.bean.AppInfo;
import app.tv.quickstart.presenter.AppInstalledPresenter;
import app.tv.quickstart.widgets.AppVerticalGridView;
import app.tv.quickstart.widgets.focus.MyItemBridgeAdapter;

/**
 * File: AppInstalledActivity
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2020/10/30 16:13
 * Description:
 */
public class AppInstalledActivity extends BaseActivity {
    private static final String TAG = "AppInstalledActivity";
    private ArrayObjectAdapter mAdapter;
    private AppVerticalGridView mVgAppInstalled;
    private TextView mTvAppNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_installed);
        initView();
        initData();
    }

    private void initView() {
        mVgAppInstalled = findViewById(R.id.vg_app_installed);
        mTvAppNumber = findViewById(R.id.tv_app_installed_number);
        mVgAppInstalled.setColumnNumbers(6);
        mVgAppInstalled.setHorizontalSpacing(FontDisplayUtil.dip2px(this, 53));
        mVgAppInstalled.setVerticalSpacing(FontDisplayUtil.dip2px(this, 20));
        mAdapter = new ArrayObjectAdapter(new AppInstalledPresenter());
        ItemBridgeAdapter itemBridgeAdapter = new MyItemBridgeAdapter(mAdapter) {

            @Override
            public MyItemBridgeAdapter.OnItemViewClickedListener getOnItemViewClickedListener() {
                return (focusView, itemViewHolder, item) -> {
                    if (focusView.hasFocus()
                            && item instanceof AppInfo) {

                        try {
                            PackageManager packageManager = getPackageManager();
                            Intent intent = packageManager
                                    .getLaunchIntentForPackage(((AppInfo) item).packageName);
                            if (intent == null) {
                                Toast.makeText(AppInstalledActivity.this,
                                        ((AppInfo) item).name + "未安装",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
            }

            @Override
            public OnItemViewLongClickedListener getOnItemViewLongClickedListener() {
                return (focusView, itemViewHolder, item) -> {
                    if (focusView.hasFocus()
                            && item instanceof AppInfo
                            && !TextUtils.isEmpty(((AppInfo) item).packageName)) {
                        Uri packageURI = Uri.parse("package:" + ((AppInfo) item).packageName);
                        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(uninstallIntent);
                    }
                    return true;
                };
            }
        };
        mVgAppInstalled.setAdapter(itemBridgeAdapter);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter,
                FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
    }

    private void initData() {
        List<AppInfo> appInfos = getInstallApps(getApplicationContext());
        if (appInfos == null) {
            return;
        }
        mTvAppNumber.setText(String.valueOf(appInfos.size()));
        mAdapter.addAll(0, appInfos);
    }

    public List<AppInfo> getInstallApps(Context context) {
        Log.e(TAG, "getInstallApps0: ");
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);  //获取所以已安装的包

        List<AppInfo> list = new ArrayList<>();
        for (PackageInfo packageInfo : installedPackages) {
            Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
            if (intent == null) {
                continue;
            }
            AppInfo info = new AppInfo();
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;  //应用信息
            info.name = applicationInfo.loadLabel(pm).toString();
            info.icon = applicationInfo.loadIcon(pm);        //状态机,通过01状态来表示是否具备某些属性和功能

            info.packageName = packageInfo.packageName;
            info.versionName = packageInfo.versionName;
            info.versionCode = packageInfo.versionCode;
            int flags = applicationInfo.flags;  //获取应用标记
            info.isRom = (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != ApplicationInfo
                    .FLAG_EXTERNAL_STORAGE;
            info.isUser = (flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo
                    .FLAG_SYSTEM;
//            Log.e(TAG, "getInstallApps: " + info.toString());
            list.add(info);
        }
        Log.e(TAG, "getInstallApps1: ");
        return list;
    }
}
