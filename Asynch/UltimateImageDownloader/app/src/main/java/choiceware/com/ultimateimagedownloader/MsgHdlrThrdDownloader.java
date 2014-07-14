package choiceware.com.ultimateimagedownloader;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by andyc on 7/3/2014.
 */
public class MsgHdlrThrdDownloader extends AbstractDownloader {

    private final static int REQ_LOAD = 1;
    private HandlerThread mHdlThrd;
    private Handler mHdlr;

    public MsgHdlrThrdDownloader() {
        super(MyApplication.getStrFromContext(R.string.MessengerLooper));

        // Start the handler thread to handle all load requests
        mHdlThrd = new HandlerThread(MsgHdlrThrdDownloader.class.getSimpleName());
        mHdlThrd.start();

        // Set up handler
        mHdlr = new Handler(mHdlThrd.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REQ_LOAD) {
                    LoadOptions opt = (LoadOptions) msg.obj;
                    try {
                        DownloaderUtils.loadImageWithCallbackSync(mCallback, opt);
                    }
                    finally {
                        notifyIsDoneLoading();
                    }
                }
            }
        };
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {
        Message msg = mHdlr.obtainMessage(REQ_LOAD, loadOpt);
        mHdlr.sendMessage(msg);
    }


}
