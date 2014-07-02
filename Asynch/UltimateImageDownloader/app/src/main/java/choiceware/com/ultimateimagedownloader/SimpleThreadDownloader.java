package choiceware.com.ultimateimagedownloader;

import android.net.Uri;

/**
 * Using a simple thread to download image
 * Created by andyc_000 on 7/2/2014.
 */
public class SimpleThreadDownloader extends AbstractDownloader {

    public SimpleThreadDownloader() {
        super("Simple Thread");
    }

    @Override
    protected void onLoad(final LoadOptions loadOpt) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DownloaderUtils.loadImageWithCallbackSync(mCallback, loadOpt.getUri(),
                            loadOpt.getMaxWidth(), loadOpt.getMaxHeight(), loadOpt.getCxlSig());
                }
                finally {
                    mIsLoading = false;
                }
            }
        });
        t.start();
    }
}
