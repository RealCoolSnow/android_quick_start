package app.quickstart.flutter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.Utils;
import com.idlefish.flutterboost.containers.BoostFlutterActivity;
import com.idlefish.flutterboost.interfaces.INativeRouter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import app.quickstart.activity.FlutterNativeActivity;
import io.flutter.embedding.android.FlutterView;
import io.flutter.plugin.common.MethodChannel;

/**
 * File: FlutterRouter
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2021/2/7 17:26
 * Description:
 */
public class FlutterRouter {
    public final static Map<String, String> pageName = new HashMap<String, String>() {{
        put("first", "first");
        put("second", "second");
        put("tab", "tab");
        put("app://flutterPage", "flutterPage");
    }};
    public static final String NATIVE_PAGE_URL = "app://nativePage";
    public static final String FLUTTER_PAGE_URL = "app://flutterPage";

    public static boolean openPageByUrl(Context context, String url, Map params, int requestCode) {
        String path = url.split("\\?")[0];
        Logger.i("openPageByUrl", path);
        try {
            if (pageName.containsKey(path)) {
                // 这直接用的Boost提供的Activity作为Flutter的容器，也可以继承BoostFlutterActivity后做一些自定义的行为
                Intent intent = BoostFlutterActivity.withNewEngine().url(pageName.get(path)).params(params)
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque).build(context);
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent, requestCode);
                } else {
                    context.startActivity(intent);
                }
                return true;
            } else if (url.startsWith(NATIVE_PAGE_URL)) {
                context.startActivity(new Intent(context, FlutterNativeActivity.class));
                return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        }
    }

    public static void init(Application app) {
        INativeRouter router = new INativeRouter() {
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                String assembleUrl = Utils.assembleUrl(url, urlParams);
                openPageByUrl(context, assembleUrl, urlParams, requestCode);
            }
        };
        FlutterBoost.BoostLifecycleListener boostLifecycleListener = new FlutterBoost.BoostLifecycleListener() {
            @Override
            public void beforeCreateEngine() {
            }

            @Override
            public void onEngineCreated() {
                // 注册MethodChannel，监听flutter侧的getPlatformVersion调用
                MethodChannel methodChannel = new MethodChannel(FlutterBoost.instance().engineProvider().getDartExecutor(), "flutter_native_channel");
                methodChannel.setMethodCallHandler((call, result) -> {
                    if (call.method.equals("getPlatformVersion")) {
                        result.success(Build.VERSION.RELEASE);
                    } else {
                        result.notImplemented();
                    }
                });
                // 注册PlatformView viewTypeId要和flutter中的viewType对应
//                FlutterBoost
//                        .instance()
//                        .engineProvider()
//                        .getPlatformViewsController()
//                        .getRegistry()
//                        .registerViewFactory("plugins.test/view", new TextPlatformViewFactory(StandardMessageCodec.INSTANCE));
            }

            @Override
            public void onPluginsRegistered() {

            }

            @Override
            public void onEngineDestroy() {

            }

        };
        //
        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
        //
        //   <meta-data android:name="flutterEmbedding"
        //               android:value="2">
        //    </meta-data>
        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
        //
        // 插件注册方式请使用
        // FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin());
        // GeneratedPluginRegistrant.registerWith()，是在engine 创建后马上执行，放射形式调用
        //
        Platform platform = new FlutterBoost
                .ConfigBuilder(app, router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build();
        FlutterBoost.instance().init(platform);
    }
}
