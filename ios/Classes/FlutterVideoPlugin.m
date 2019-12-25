#import "FlutterVideoPlugin.h"
#import <flutter_video/flutter_video-Swift.h>

@implementation FlutterVideoPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterVideoPlugin registerWithRegistrar:registrar];
}
@end
