package choiceware.com.ultimateimagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UnknownFormatConversionException;

/**
 * Convenience functions to deal with Message
 *
 * Created by andyc on 7/15/2014.
 */
public class MessageUtils {

    private static final String TAG = MessageUtils.class.getSimpleName();
    private static String mFolder;
    private static final int WHAT_OK = 0;
    private static final int WHAT_ERROR = 1;
    private static final int WHAT_CANCELLED = 2;

    /**
     * Gets folder to use for all file related operations
     * @return
     */
    public static String getFolder() {
        return mFolder;
    }

    /**
     * Sets folder to use for all file related operations
     * @param folder
     */
    public static void setFolder(String folder) {
        mFolder = folder;
    }

    /**
     * Convenience function to fill a cancelled message
     * @param msg
     */
    public static void setCancelled(Message msg) {
        msg.what = WHAT_CANCELLED;
    }

    public static boolean isCancelled(Message msg) {
        return msg.what == WHAT_CANCELLED;
    }

    /**
     * Convenience function to fill an error message
     * @param msg
     * @param ex
     */
    public static void setError(Message msg, Exception ex) {
        msg.what = WHAT_ERROR;
        msg.obj = ex;
    }

    public static Exception getError(Message msg) {
        if (msg.what != WHAT_ERROR) {
            return null;
        }
        return (Exception) msg.obj;
    }

    /**
     * Saves image into a file and then sets field name of msg to pathname of file.  See also
     * getBitmap.
     * @param image
     * @param msg
     * @param fieldName
     * @throws Exception
     */
    public static void setBitmap(Bitmap image, Message msg, String fieldName) throws Exception {

        // Uses default field name if none is provided
        if (fieldName == null) {
            fieldName = IntentExtraData.IMAGE;
        }

        // Creates temporary filename
        File file;
        if (mFolder == null) {
            file = File.createTempFile("image", "png");
        }
        else {
            file = File.createTempFile("image", "png", new File(mFolder));
        }

        // Save image into file
        DownloaderUtils.saveImage(image, file);

        // Sets field name within message to temporary file name
        msg.getData().putString(fieldName, file.getPath());
        msg.what = WHAT_OK;
    }

    /**
     * Gets pathname from field name of msg and converts into bitmap.  See setBitmap.
     * @param msg
     * @param fieldName
     * @param deleteFile
     * @return
     * @throws Exception
     */
    public static Bitmap getBitmap(Message msg, String fieldName, boolean deleteFile) throws Exception {

        // Uses default field name if none is provided
        if (fieldName == null) {
            fieldName = IntentExtraData.IMAGE;
        }

        // Gets image pathname
        String file = msg.getData().getString(fieldName);
        Log.d(TAG, "Got image path " + file);

        // Decodes bitmap
        return DownloaderUtils.loadImage(new File(file), deleteFile);
    }
}
