package choiceware.com.ultimateimagedownloader;

import android.net.Uri;

/**
 * Created by achoi on 7/1/2014.
 */
public interface IDownloader {

    String toString();

    IDownloaderCallback getCallback();

    void setCallback(IDownloaderCallback cb);

    void load(Uri uri, int maxWidth, int maxHeight);

    boolean isLoading();

    void requestCancel();
}
