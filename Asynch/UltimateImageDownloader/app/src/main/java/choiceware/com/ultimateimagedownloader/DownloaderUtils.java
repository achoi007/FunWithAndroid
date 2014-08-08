package choiceware.com.ultimateimagedownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UnknownFormatConversionException;

/**
 * Created by achoi on 7/1/2014.
 */
public class DownloaderUtils {

    private static final String TAG = DownloaderUtils.class.getSimpleName();

    /**
     * Save image into given filename.  See also loadImage
     * @param image
     * @param file
     * @throws Exception
     */
    public static void saveImage(Bitmap image, File file) throws Exception {
        // Save bitmap into file
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            boolean decodable = image.compress(Bitmap.CompressFormat.PNG, 0, os);
            if (!decodable) {
                Exception ex = new UnknownFormatConversionException("format not decodable");
                Log.e(TAG, ex.getMessage(), ex);
                throw ex;
            }
        }
        finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * Load bitmap from given file.  See also saveImage
     * @param file
     * @param deleteFile
     * @return
     * @throws Exception
     */
    public static Bitmap loadImage(File file, boolean deleteFile) throws Exception {
        Bitmap image = BitmapFactory.decodeFile(file.getPath());
        if (deleteFile) {
            file.delete();
        }
        return image;
    }

    /**
     * Same as call loadImageSync followed by saveImageAsFile
     * @param folder
     * @param uri
     * @param maxWidth
     * @param maxHeight
     * @param cxlSig
     * @return
     * @throws Exception
     */
    public static File loadImageAsFileSync(File folder, Uri uri, int maxWidth, int maxHeight,
                                           CancellationSignal cxlSig) throws Exception {
        // Load image
        Bitmap image = loadImageSync(uri, maxWidth, maxHeight, cxlSig);

        // Create temp file
        File tmpFile;
        if (folder == null) {
            tmpFile = File.createTempFile("image", "png");
        }
        else {
            tmpFile = File.createTempFile("image", "png", folder);
        }

        // Save bitmap into file
        saveImage(image, tmpFile);

        return tmpFile;
    }

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
