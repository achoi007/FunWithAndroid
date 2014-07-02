package choiceware.com.ultimateimagedownloader;

import android.net.Uri;

/**
 * Created by alang_000 on 7/2/2014.
 */
public class SyncDownloader extends AbstractDownloader {

    public SyncDownloader() {
        super("Synchronous");
    }

    @Override
    public void load(Uri uri, int maxWidth, int maxHeight) {
        mIsLoading = true;
        DownloaderUtils.loadImageWithCallbackSync(mCallback, uri, maxWidth, maxHeight);
        mIsLoading = false;
    }
}
