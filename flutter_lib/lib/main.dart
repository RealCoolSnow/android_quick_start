/*
 * @Description: 
 * @Author: CoolSnow (coolsnow2020@gmail.com)
 * @Date: 2020-09-08 17:59:49
 * @LastEditors: CoolSnow
 * @LastEditTime: 2020-09-10 14:23:24
 */
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_lib/app_sentry.dart';
import 'package:flutter_lib/ui/app.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SystemChrome.setPreferredOrientations(<DeviceOrientation>[
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown
  ]).then((_) => AppSentry.runWithCatchError(App()));
}
