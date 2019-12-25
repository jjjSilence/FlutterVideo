package jjj.com.flutter_video;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

//    String path = "rtmp://58.200.131.2:1935/livetv/hunantv";
public class VideoManager {
    //正常
    public static final int CURRENT_STATE_NORMAL = 0;
    //准备中
    public static final int CURRENT_STATE_PREPAREING = 1;
    //播放中
    public static final int CURRENT_STATE_PLAYING = 2;
    //暂停
    public static final int CURRENT_STATE_PAUSE = 3;
    //错误状态
    public static final int CURRENT_STATE_ERROR = 4;
    //播放结束
    public static final int CURRENT_STATE_COMPLETION = 5;
    //无网状态
    public static final int CURRENT_STATE_NONETWORK = 6;
    //开始缓冲
    public static final int CURRENT_STATE_BUFFER_START = 7;
    //结束缓冲
    public static final int CURRENT_STATE_BUFFER_END = 8;

    protected int mCurrentState = -1;

    private Surface mSurface;
    private String mVideoUrl;
    private IjkMediaPlayer mPlayer;

    private Context mContext;

    private OnPrepareListener mOnPrepareListener;
    private OnErrorListener mOnErrorListener;


    public VideoManager(Context context) {
        mContext = context;
        mPlayer = new IjkMediaPlayer();
    }

    public void setVideoUrl(final String url) {
        if (!TextUtils.isEmpty(mVideoUrl) && mPlayer != null) {
            dispose();
        }

        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
        }

        if (mSurface != null) {
            mVideoUrl = url;
            mPlayer.setSurface(mSurface);

            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
                @Override
                public boolean onNativeInvoke(int i, Bundle bundle) {
                    Log.e("------------------d", "onNativeInvoke" + i);
                    return true;
                }
            });
            mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.e("------------------d", "onPrepared");
                    if (mOnPrepareListener != null) {
                        mOnPrepareListener.prepare();
                    }
                }
            });
            mPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                    Log.e("------------------d", "onInfo" + what);
//                    if (what == MEDIA_INFO_VIDEO_RENDERING_START) {
//                        start();
//                    } else if (what == MEDIA_INFO_BUFFERING_START) {
//                        pause();
//                    } else if (what == MEDIA_INFO_BUFFERING_END) {
//                        start();
//                    }
                    return false;
                }
            });
            mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                    Log.e("------------------d", "onError" + what);
                    if (mOnErrorListener != null) {
                        mOnErrorListener.error();
                    }
                    return false;
                }
            });
            mPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int what) {
                    Log.e("------------------d", "OnBufferingUpdat" + what);
                }
            });
            mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.e("------------------d", "onCompletion");
                }
            });

            try {
                mPlayer.setDataSource(mContext, Uri.parse(mVideoUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void prepareAsync(OnPrepareListener onPrepareListener) {
        mOnPrepareListener = onPrepareListener;
        mPlayer.prepareAsync();
    }

    void setOnErrorListener(OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    void start() {
        mPlayer.start();
    }

    void pause() {
        mPlayer.pause();
    }

    void dispose() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    void setSurface(Surface surface) {
        mSurface = surface;
    }

    interface OnPrepareListener {
        void prepare();
    }

    interface OnErrorListener {
        void error();
    }
}

