package choiceware.com.ultimateimagedownloader;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * An intent service to download image.  It takes Uri, max width, max height, and messenger from
 * intent and tells messenger the pathname of downloaded image.
 *
 * Created by andyc on 7/13/2014.
 */
public class IntentServiceMsgDownloaderService extends IntentService {

    private static final String TAG = IntentServiceMsgDownloaderService.class.getSimpleName();

    public IntentServiceMsgDownloaderService() {
        super("IntentServiceMsgDownloaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Get parameters
        Uri uri = intent.getData();
        int maxWidth = IntentExtraData.getMaxWidth(intent);
        int maxHeight = IntentExtraData.getMaxHeight(intent);
        Log.d(TAG, "onHandleIntent: " + uri + " " + maxWidth + "x" + maxHeight);

        try {
            // Download bitmap
            Bitmap image = DownloaderUtils.loadImageSync(uri, maxWidth, maxHeight,
                    new CancellationSignal());

            // Generate message to send
            Message msg = Message.obtain();
            DownloaderUtils.setBitmapIntoMessage(msg, null, image);

            // Send message
            Messenger messenger = IntentExtraData.getMessenger(intent);
            messenger.send(msg);
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
    }
}
