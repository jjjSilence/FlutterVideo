import 'dart:async';

import 'package:flutter/services.dart';

class VideoController {
  static const MethodChannel _channel =
      const MethodChannel('flutter_video');

  int textureId;

  Future<int> initialize(double width, double height, String url) async {
    textureId = await _channel.invokeMethod('create', {
      'width': width,
      'height': height,
      'url': url,
    });
    return textureId;
  }

  Future<Null> start() async {
    await _channel.invokeMethod("start", {'textureId': textureId});
  }

  Future<Null> pause() async {
    await _channel.invokeMethod("pause", {'textureId': textureId});
  }

  Future<Null> error() async {
    await _channel.invokeMethod("error", {'textureId': textureId});
  }

  Future<Null> dispose() =>
      _channel.invokeMethod('dispose', {'textureId': textureId});

  bool get isInitialized => textureId != null;
}
