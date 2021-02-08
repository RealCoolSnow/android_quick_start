import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';
import 'package:flutter_lib/ui/page/home.dart';

class FlutterRouter {
  static void init() {
    FlutterBoost.singleton.registerPageBuilders(<String, PageBuilder>{
      // 'embeded': (String pageName, Map<String, dynamic> params, String _) =>
      //     EmbeddedFirstRouteWidget(),
      // 'first': (String pageName, Map<String, dynamic> params, String _) =>
      //     FirstRouteWidget(),
      // 'firstFirst': (String pageName, Map<String, dynamic> params, String _) =>
      //     FirstFirstRouteWidget(),
      // 'second': (String pageName, Map<String, dynamic> params, String _) =>
      //     SecondRouteWidget(),
      // 'secondStateful':
      //     (String pageName, Map<String, dynamic> params, String _) =>
      //         SecondStatefulRouteWidget(),
      // 'tab': (String pageName, Map<String, dynamic> params, String _) =>
      //     TabRouteWidget(),
      // 'platformView':
      //     (String pageName, Map<String, dynamic> params, String _) =>
      //         PlatformRouteWidget(),
      // 'flutterFragment':
      //     (String pageName, Map<String, dynamic> params, String _) =>
      //         FragmentRouteWidget(params),

      ///可以在native层通过 getContainerParams 来传递参数
      'flutterPage': (String pageName, Map<String, dynamic> params, String _) {
        print('flutterPage params:$params');

        return HomePage();
      },
    });
    FlutterBoost.singleton
        .addBoostNavigatorObserver(FlutterBoostNavigatorObserver());
  }
}

class FlutterBoostNavigatorObserver extends NavigatorObserver {
  @override
  void didPush(Route<dynamic> route, Route<dynamic> previousRoute) {
    print('flutterboost#didPush');
  }

  @override
  void didPop(Route<dynamic> route, Route<dynamic> previousRoute) {
    print('flutterboost#didPop');
  }

  @override
  void didRemove(Route<dynamic> route, Route<dynamic> previousRoute) {
    print('flutterboost#didRemove');
  }

  @override
  void didReplace({Route<dynamic> newRoute, Route<dynamic> oldRoute}) {
    print('flutterboost#didReplace');
  }
}
