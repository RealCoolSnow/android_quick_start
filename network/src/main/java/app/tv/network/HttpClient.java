package app.tv.network;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * File: HttpClient
 * Author: CoolSnow(coolsnow2020@gmail.com)
 * Created at: 2020/10/30 18:07
 * Description:
 */
public class HttpClient {
    public static final String TAG = "HttpClient";

    public static final int CONNECT_TIMEOUT = 20;
    public static final int READ_TIMEOUT = 20;
    public static final int WRITE_TIMEOUT = 20;

    private final OkHttpClient mOkHttpClient;

    private static class MyCookieJar implements CookieJar {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            Logger.d(TAG, "saveCookieFromResponse " + url.toString() + " " + cookies.toString());
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = new ArrayList<>();
            Logger.d(TAG, "loadCookieForRequest " + url.host().toString() + "  " + (cookies == null ? "" : cookies.toString()));
            return cookies;
        }
    }

    private HttpClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(new MyCookieJar())
                .build();
    }

    @NonNull
    public static HttpClient getInstance() {
        return Holder.instance;
    }

    /**
     * 异步请求
     *
     * @param request
     * @param callback
     */
    public void call(Request request, Callback callback) {
        Logger.d(TAG, "request:" + request.toString());
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private static class Holder {
        private static HttpClient instance = new HttpClient();
    }
}
