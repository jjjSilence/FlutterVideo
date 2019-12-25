import 'package:flutter/material.dart';
import 'package:flutter_video/VideoController.dart';
import 'package:flutter_video/VideoView.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
        home: new Scaffold(
            appBar: new AppBar(
              title: new Text('Video example'),
            ),
            body: Column(
              children: <Widget>[
                Text(""),
                VideoView(
                  url: "rtmp://58.200.131.2:1935/livetv/hunantv",
                  controller: VideoController(),
                ),
              ],
            )));
  }
}
