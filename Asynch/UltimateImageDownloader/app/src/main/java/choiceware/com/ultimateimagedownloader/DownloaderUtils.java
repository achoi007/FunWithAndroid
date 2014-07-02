package choiceware.com.ultimateimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by alang_000 on 7/1/2014.
 */
public class DownloaderUtils {

    private static final String TAG = DownloaderUtils.class.getSimpleName();

    /***
     * Load image synchronously.  Will block current thread.
     * @param uri
     * @return
     */
    public static Bitmap loadImageSync(Uri uri, int maxWidth, int maxHeight) throws Exception {
        // Get uri as input stream
        URL url = new URL(uri.toString());
        InputStream urlStream = url.openStream();

        try {
            // Load image
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(urlStream, false);
            Bitmap image = decoder.decodeRegion(new Rect(0, 0, maxWidth, maxHeight), null);
            Log.d(TAG, "Loaded image with size: " + image.getWidth() + "x" + image.getHeight());
            return image;
        }
        finally {
            urlStream.close();
        }
    }

    /***
     * Helper function to load image synchronously and either call onImage of callback if success or
     * onError of callback if error
     * @param cb
     * @param uri
     */
    public static void loadImageWithCallbackSync(IDownloaderCallback cb, Uri uri, int maxWidth,
                                                 int maxHeight) {
        try {
            Bitmap image = loadImageSync(uri, maxWidth, maxHeight);
            cb.onImage(image);
        }
        catch (Exception ex) {
            cb.onError(ex);
        }
    }
}
