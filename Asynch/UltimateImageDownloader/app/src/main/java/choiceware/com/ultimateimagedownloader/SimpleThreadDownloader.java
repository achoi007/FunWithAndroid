package choiceware.com.ultimateimagedownloader;

import android.net.Uri;

/**
 * Using a simple thread to download image
 * Created by andyc on 7/2/2014.
 */
public class SimpleThreadDownloader extends AbstractDownloader {

    public SimpleThreadDownloader() {
        super(MyApplication.getStrFromContext(R.string.SimpleThread));
    }

    @Override
    protected void onLoad(final LoadOptions loadOpt) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DownloaderUtils.loadImageWithCallbackSync(mCallback, loadOpt);
                } finally {
                    notifyIsDoneLoading();
                }
            }
        });
        t.start();
    }
}
