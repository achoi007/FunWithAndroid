package choiceware.com.ultimateimagedownloader;

import android.graphics.Bitmap;

/**
 * Created by achoi on 7/1/2014.
 */
public interface IDownloaderCallback {

    void onImage(Bitmap image);

    void onError(Exception ex);

    void onProgress(int percentComplete);

    void onCancelled();
}
