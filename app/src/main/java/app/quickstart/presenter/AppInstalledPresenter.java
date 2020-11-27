package app.quickstart.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.leanback.widget.Presenter;

import app.tv.common.util.FontDisplayUtil;
import app.tv.common.util.ImageUtil;
import app.quickstart.R;
import app.quickstart.bean.AppInfo;

/**
 * File: AppInstalledPresenter
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2020/10/30 15:34
 * Description:
 */
public class AppInstalledPresenter extends Presenter {
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_app_installed, parent, false);
        view.setOnFocusChangeListener((v, hasFocus) -> v.findViewById(R.id.tv_app_name).setSelected(hasFocus));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof AppInfo) {
            ViewHolder vh = (ViewHolder) viewHolder;
            if (((AppInfo) item).icon != null) {
                Bitmap bitmap = ImageUtil.getBitmapFromDrawable(((AppInfo) item).icon);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), bitmap);
                drawable.setCornerRadius(FontDisplayUtil.dip2px(mContext, 10));
                vh.mIvAppIcon.setImageDrawable(drawable);
            }
            if (!TextUtils.isEmpty(((AppInfo) item).name)) {
                vh.mTvAppName.setText(((AppInfo) item).name);
            }
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }


    public static class ViewHolder extends Presenter.ViewHolder {

        private final ImageView mIvAppIcon;
        private final TextView mTvAppName;

        public ViewHolder(View view) {
            super(view);
            mIvAppIcon = view.findViewById(R.id.iv_app_icon);
            mTvAppName = view.findViewById(R.id.tv_app_name);
        }
    }
}
