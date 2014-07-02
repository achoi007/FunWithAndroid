package choiceware.com.ultimateimagedownloader;

import android.net.Uri;

/**
 * Created by alang_000 on 7/2/2014.
 */
public abstract class AbstractDownloader implements IDownloader {

    protected boolean mIsLoading = false;
    protected IDownloaderCallback mCallback;
    private String mName;

    protected AbstractDownloader(String name) {
        mName = name;
    }

    @Override
    public IDownloaderCallback getCallback() {
        return mCallback;
    }

    @Override
    public void setCallback(IDownloaderCallback cb) {
        mCallback = cb;
    }

    @Override
    public abstract void load(Uri uri, int maxWidth, int maxHeight);

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public String toString() {
        return mName;
    }
}
