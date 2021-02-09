/*
 * @Description: 
 * @Author: CoolSnow (coolsnow2020@gmail.com)
 * @Date: 2020-09-09 10:38:59
 * @LastEditors: CoolSnow
 * @LastEditTime: 2020-09-21 10:44:48
 */
import 'dart:ui';
import 'dart:io';
import 'package:fluro/fluro.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:flutter_lib/ui/app_theme.dart';
import 'package:flutter_lib/ui/page/home.dart';
import 'package:flutter_lib/ui/page/splash_screen.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_lib/config/config.dart';
import 'package:flutter_lib/config/pref_key.dart';
import 'package:flutter_lib/config/route/routes.dart';
import 'package:flutter_lib/locale/i18n.dart';
import 'package:flutter_lib/locale/locale_util.dart';
import 'package:flutter_lib/storage/Pref.dart';
import 'package:flutter_lib/util/log_util.dart';
import 'package:flutter_lib/util/time_util.dart';
import 'package:flutter_lib/flutter_router.dart';

class App extends StatefulWidget {
  _AppState createState() => new _AppState();
}

class _AppState extends State<App> {
  _AppState() {
    //---router
    final router = FluroRouter();
    Routes.configureRoutes(router);
    Config.router = router;
    //---shared preferences
    Pref.setString(PrefKey.launchTime, TimeUtil.format(DateTime.now()));
    //---logutil
    logUtil.setEnabled(Config.debug);
    logUtil.d("App created");
  }
  @override
  void initState() {
    super.initState();
    FlutterRouter.init();
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.dark,
      statusBarBrightness:
          Platform.isAndroid ? Brightness.dark : Brightness.light,
      systemNavigationBarColor: Colors.white,
      systemNavigationBarDividerColor: Colors.grey,
      systemNavigationBarIconBrightness: Brightness.dark,
    ));
    return MaterialApp(
        title: Config.app,
        debugShowCheckedModeBanner: false,
        theme: new ThemeData(
          primarySwatch: AppTheme.primary,
          splashColor: AppTheme.splash,
        ),
        localizationsDelegates: [
          const I18nDelegate(),
          GlobalMaterialLocalizations.delegate,
          GlobalWidgetsLocalizations.delegate,
        ],
        supportedLocales: localeUtil.supportedLocales(),
        onGenerateRoute: Config.router.generator,
        builder: FlutterBoost.init(postPush: _onRoutePushed),
        home: Container(color: Colors.white));
  }

  void _onRoutePushed(
    String pageName,
    String uniqueId,
    Map<String, dynamic> params,
    Route<dynamic> route,
    Future<dynamic> _,
  ) {}
  _buildSplashScreen() {
    return SplashScreen(
        seconds: 3,
        navigateAfterSeconds: HomePage(),
        title: Text('flutter_easy',
            style: TextStyle(
                fontWeight: FontWeight.w700, fontSize: 20, color: Colors.pink)),
        imageBackground: AssetImage('assets/images/splash.jpg'),
        icon: AssetImage('assets/images/avatar.jpg'),
        backgroundColor: Colors.white,
        photoSize: 60.0,
        loaderColor: Colors.white);
  }
}
