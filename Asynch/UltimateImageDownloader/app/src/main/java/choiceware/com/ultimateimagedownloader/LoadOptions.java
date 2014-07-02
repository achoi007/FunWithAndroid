package choiceware.com.ultimateimagedownloader;

import android.net.Uri;
import android.os.CancellationSignal;

/**
 * Package parameters to IDownloader.load function as a single object.  Nice for things like
 * AsyncTask which only takes 1 argument
 * Created by andyc_000 on 7/2/2014.
 */
public class LoadOptions {

    private Uri mUri;
    private int mMaxWidth, mMaxHeight;
    private CancellationSignal mCxlSig;

    public LoadOptions(Uri uri, int maxWidth, int maxHeight, CancellationSignal cxlSig) {
        mUri = uri;
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        mCxlSig = cxlSig;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(int mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public void setMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }

    public CancellationSignal getCxlSig() {
        return mCxlSig;
    }

    public void setCxlSig(CancellationSignal mCxlSig) {
        this.mCxlSig = mCxlSig;
    }
}
