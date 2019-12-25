import 'package:flutter/material.dart';
import 'package:flutter_video/VideoController.dart';

class VideoView extends StatefulWidget {
  String url;
  VideoController controller;

  VideoView({this.url, this.controller}) {
    if (controller == null) {
      controller = VideoController();
    }
  }

  @override
  _VideoViewState createState() => new _VideoViewState();
}

class _VideoViewState extends State<VideoView> with WidgetsBindingObserver {
  final _width = 400.0;
  final _height = 200.0;
  var progressVisible = true;
  var errorVisible = false;

  @override
  initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    print("--eee---initState");
    initializeController();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    print("--eee" + state.toString());
    switch (state) {
      case AppLifecycleState.inactive: // 处于这种状态的应用程序应该假设它们可能在任何时候暂停。
        print("-------inactive");
        break;
      case AppLifecycleState.resumed: // 应用程序可见，前台
        print("-------resumed");
        widget.controller.start();
        break;
      case AppLifecycleState.paused: // 应用程序不可见，后台
        print("-------paused");
        widget.controller.pause();
        break;
      case AppLifecycleState.suspending: // 申请将暂时暂停
        print("-------suspending");
        break;
    }
  }

  @override
  void dispose() {
    widget.controller.dispose();
    print("--eee---dispose");
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Stack(
          alignment: Alignment.center,
          children: <Widget>[
            Container(
              width: _width,
              height: _height,
              color: Colors.black,
              child: widget.controller.isInitialized
                  ? new Texture(textureId: widget.controller.textureId)
                  : null,
            ),
            Opacity(
              opacity: progressVisible ? 1 : 0,
              child: CircularProgressIndicator(
                strokeWidth: 4.0,
                backgroundColor: Colors.blue,
                valueColor: new AlwaysStoppedAnimation<Color>(Colors.red),
              ),
            ),
            Opacity(
              opacity: errorVisible ? 1 : 0,
              child: Image(
                image: AssetImage("images/video_error.png"),
              ),
            )
          ],
        ),
        RaisedButton(
          child: Text("开始"),
          onPressed: () {
            start();
          },
        ),
        RaisedButton(
          child: Text("暂停"),
          onPressed: () {
            pause();
          },
        ),
      ],
    );
  }

  Future<Null> initializeController() async {
    await widget.controller.initialize(_width, _height, widget.url);
    progressVisible = false;
    print("-------------initializeController");
    setState(() {});
  }

  Future<Null> start() async {
    await widget.controller.start();
    setState(() {});
  }

  Future<Null> pause() async {
    await widget.controller.pause();
    setState(() {});
  }

  Future<Null> error() async {
    await widget.controller.error();
    setState(() {});
  }
}
