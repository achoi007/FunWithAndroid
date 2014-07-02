package choiceware.com.ultimateimagedownloader;

import android.net.Uri;
import android.os.CancellationSignal;

/**
 * Created by alang_000 on 7/2/2014.
 */
public abstract class AbstractDownloader implements IDownloader {

    protected boolean mIsLoading = false;
    protected IDownloaderCallback mCallback;
    private String mName;
    private CancellationSignal mCxlSig;

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
    public void load(Uri uri, int maxWidth, int maxHeight) {
        mIsLoading = true;
        mCxlSig = new CancellationSignal();
        LoadOptions loadOpt = new LoadOptions(uri, maxWidth, maxHeight, mCxlSig);
        onLoad(loadOpt);
    }

    @Override
    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public void requestCancel() {
        mCxlSig.cancel();
        onRequestCancel();
    }

    /***
     * Hook function to handle when onLoad is called.  mLoading is set to true.
     * @param loadOpt
     */
    protected abstract void onLoad(LoadOptions loadOpt);

    /***
     * Hook function to handle when requestCancel is called.  mCxlSig.isCancelled() should be
     * true already.
     */
    protected void onRequestCancel() {

    }
}
