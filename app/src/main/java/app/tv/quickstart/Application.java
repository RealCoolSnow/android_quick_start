package app.tv.quickstart;


import com.orhanobut.logger.Logger;

import app.tv.logger.LoggerConfig;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoggerConfig.init(this, Constants.LOGGER_TAG, Constants.DEBUG);
        Logger.i("app created");
    }
}
