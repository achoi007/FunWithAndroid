package choiceware.com.ultimateimagedownloader;

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

    /**
     * Function takes a bitmap and saves it into a temporary file, and then sets
     * fieldName of Message to temporary file name.  See also getBitmapFromMessage.
     * @param msg
     * @param fieldName - field name to use in msg, or null to use IntentExtraData.IMAGE
     * @param image
     * @throws Exception
     */
    public static void setBitmapIntoMessage(Message msg, String fieldName, Bitmap image)
        throws Exception {

        // Use IMAGE if no field name specified
        if (fieldName == null) {
            fieldName = IntentExtraData.IMAGE;
        }

        // Create temporary file name
        File file = File.createTempFile("image", "png");
        Log.d(TAG, "Creating temp file " + file.getPath());

        // Write bitmap into temporary file
        Log.d(TAG, "Writing bitmap to temp file");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            boolean isDecodable = image.compress(Bitmap.CompressFormat.PNG, 100, os);
            if (!isDecodable) {
                Exception ex = new UnknownFormatConversionException("format is not decodable");
                Log.e(TAG, ex.getMessage(), ex);
                throw ex;
            }
        }
        finally {
            os.close();
        }

        // Set field name to temporary file name
        Bundle bundle = new Bundle();
        bundle.putString(fieldName, file.getPath());
        msg.setData(bundle);
        Log.d(TAG, "Setting temp file into bundle");
    }

    /**
     * Convenience function to get image pathname from message field and return bitmap.
     * Optionally delete the image file if no error occurs.  See also setBitmapToMessage.
     * @param msg
     * @param fieldName - field in msg containing image path, or use IntentExtraData.IMAGE if null.
     * @param deleteFile - if true, delete image file if no error
     * @return
     * @throws Exception
     */
    public static Bitmap getBitmapFromMessage(Message msg, String fieldName, boolean deleteFile)
        throws Exception {

        // Use IMAGE if no field name specified
        if (fieldName == null) {
            fieldName = IntentExtraData.IMAGE;
        }

        // Get file name
        String path = msg.getData().getString(fieldName);
        Log.d(TAG, "Got bitmap path " + path);

        // Create bitmap from file
        Bitmap image = BitmapFactory.decodeFile(path);

        // Check if delete file
        if (deleteFile) {
            Log.d(TAG, "Deleting bitmap file");
            File file = new File(path);
            file.delete();
        }

        return image;
    }
}
