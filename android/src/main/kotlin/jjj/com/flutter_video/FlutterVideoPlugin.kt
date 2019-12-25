package jjj.com.flutter_video

import android.app.Activity
import android.util.Log
import android.util.LongSparseArray
import android.view.Surface
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.TextureRegistry

class FlutterVideoPlugin(val mContext: Activity, val mTextures: TextureRegistry) : MethodCallHandler {
    private var mSurface: Surface? = null
    private var mVideoManager: VideoManager? = null
    private val renders: LongSparseArray<VideoManager> = LongSparseArray<VideoManager>()

    override fun onMethodCall(call: MethodCall, result: Result) {
        val arguments = call.arguments as Map<String, Any?>
        Log.d("eeeeeee", call.method + " " + call.arguments.toString())
        if (call.method == "create") {
            if (arguments.containsKey("width") && arguments.containsKey("height") && arguments.containsKey("url")) {
                val width: Double = arguments["width"] as Double
                val height: Double = arguments["height"] as Double
                val url: String = arguments["url"] as String

                val entry = mTextures.createSurfaceTexture()
                val surfaceTexture = entry.surfaceTexture()
                surfaceTexture.setDefaultBufferSize(width.toInt(), height.toInt())
                mSurface = Surface(surfaceTexture)

                if (mVideoManager != null) mVideoManager!!.dispose()
                mVideoManager = VideoManager(mContext)
                mVideoManager!!.setSurface(mSurface)
                mVideoManager!!.setVideoUrl(url)
                mVideoManager!!.prepareAsync { result.success(entry.id()) }
                mVideoManager!!.setOnErrorListener { result.success("") }

                renders.put(entry.id(), mVideoManager)
            }
        } else if (call.method == "dispose") {
            if (arguments.containsKey("textureId")) {
                val textureId: Long = arguments["arguments"] as Long
                val render = renders[textureId]
                render.dispose()
                renders.delete(textureId)
            }
        } else if (call.method == "start") {
            if (mVideoManager != null) {
                mVideoManager!!.start()
            }
        } else if (call.method == "pause") {
            if (mVideoManager != null) {
                mVideoManager!!.pause()
            }
        } else if (call.method == "error") {
            if (mVideoManager != null) {
                mVideoManager!!.setOnErrorListener { result.success(mTextures.createSurfaceTexture().id()) }
            }
        } else {
            result.notImplemented()
        }
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_video")
            channel.setMethodCallHandler(FlutterVideoPlugin(registrar.activity(), registrar.textures()))
        }
    }
}
