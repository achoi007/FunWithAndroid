package choiceware.com.ultimateimagedownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.IllegalFormatException;
import java.util.UnknownFormatConversionException;

/**
 * Created by andyc on 7/1/2014.
 */
public class DownloaderUtils {

    private static final String TAG = DownloaderUtils.class.getSimpleName();

    /**
     * Load image synchronously.  Will block current thread.
     * Return bitmap if bitmap is loaded or null if cancelled.
     *
     * @param uri
     * @return
     */
    public static Bitmap loadImageSync(Uri uri, int maxWidth, int maxHeight,
                                       CancellationSignal cxlSig) throws Exception {

        // Set up handling of cancellation signal
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        cxlSig.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                opts.requestCancelDecode();
            }
        });

        // Get uri as input stream
        URL url = new URL(uri.toString());
        InputStream urlStream = url.openStream();

        try {
            Log.d(TAG, "Trying to load uri " + url + " with size constraint " + maxWidth + "x" +
                    maxHeight);
            // Load image
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(urlStream, false);
            Bitmap image = decoder.decodeRegion(new Rect(0, 0, maxWidth, maxHeight), opts);
            if (image != null) {
                Log.d(TAG, "Loaded image with size: " + image.getWidth() + "x" + image.getHeight());
            }
            return image;
        } finally {
            urlStream.close();
        }
    }

    /**
     * Convenience function to load bitmap from using information in intent
     * @param intent
     * @param cxlSig
     */
    public static Bitmap loadImageSync(Intent intent, CancellationSignal cxlSig) throws Exception {
        Uri uri = intent.getData();
        int maxWidth = IntentExtraData.getMaxWidth(intent);
        int maxHeight = IntentExtraData.getMaxHeight(intent);
        Log.d(TAG, "load img from intent " + uri + ":" + maxWidth + "x" + maxHeight);
        return loadImageSync(uri, maxWidth, maxHeight, cxlSig);
    }

    /**
     * Helper function to load image synchronously and either call onImage of callback if success or
     * onError of callback if error
     *
     * @param cb
     * @param uri
     */
    public static void loadImageWithCallbackSync(IDownloaderCallback cb, Uri uri, int maxWidth,
                                                 int maxHeight, CancellationSignal cxlSig) {
        try {
            // FINISH - handle onProgress

            Bitmap image = loadImageSync(uri, maxWidth, maxHeight, cxlSig);
            if (image == null) {
                cb.onCancelled();
            } else {
                cb.onImage(image);
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            cb.onError(ex);
        }
    }

    /**
     * Convenience function to call loadImageWithCallbackSync with LoadOptions
     * @param cb
     * @param loadOpt
     */
    public static void loadImageWithCallbackSync(IDownloaderCallback cb, LoadOptions loadOpt)
    {
        loadImageWithCallbackSync(cb, loadOpt.getUri(), loadOpt.getMaxWidth(),
                loadOpt.getMaxHeight(), loadOpt.getCxlSig());
    }
}
