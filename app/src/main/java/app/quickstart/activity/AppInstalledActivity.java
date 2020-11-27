package app.quickstart.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.ItemBridgeAdapter;

import java.util.ArrayList;
import java.util.List;

import app.common.util.FontDisplayUtil;
import app.quickstart.base.BaseActivity;
import app.quickstart.bean.AppInfo;
import app.quickstart.databinding.ActivityAppInstalledBinding;
import app.quickstart.presenter.AppInstalledPresenter;
import app.quickstart.widgets.focus.MyItemBridgeAdapter;

/**
 * File: AppInstalledActivity
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2020/10/30 16:13
 * Description:
 */
public class AppInstalledActivity extends BaseActivity<ActivityAppInstalledBinding> {
    private static final String TAG = "AppInstalledActivity";
    private ArrayObjectAdapter mAdapter;

    @Override
    protected void initView() {
        binding.vgAppInstalled.setColumnNumbers(6);
        binding.vgAppInstalled.setHorizontalSpacing(FontDisplayUtil.dip2px(this, 53));
        binding.vgAppInstalled.setVerticalSpacing(FontDisplayUtil.dip2px(this, 20));
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
        binding.vgAppInstalled.setAdapter(itemBridgeAdapter);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter,
                FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
        initData();
    }

    private void initData() {
        List<AppInfo> appInfos = getInstallApps(getApplicationContext());
        if (appInfos == null) {
            return;
        }
        binding.tvAppInstalledNumber.setText(String.valueOf(appInfos.size()));
        mAdapter.addAll(0, appInfos);
    }

    public List<AppInfo> getInstallApps(Context context) {
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
            list.add(info);
        }
        return list;
    }
}
