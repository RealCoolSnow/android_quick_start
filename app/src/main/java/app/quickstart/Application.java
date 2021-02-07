package app.quickstart;


import com.orhanobut.logger.Logger;

import app.logger.LoggerConfig;
import app.quickstart.flutter.FlutterRouter;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoggerConfig.init(this, Constants.LOGGER_TAG, Constants.DEBUG);
        FlutterRouter.init(this);
        Logger.i("app created");
    }
}
