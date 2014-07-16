package choiceware.com.ultimateimagedownloader;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A service which uses thread pool to download image and communicates result back
 * via messenger.
 *
 * Created by andyc on 7/15/2014.
 */
public class ThreadPoolMsgDownloaderService extends Service {

    private static final String TAG = ThreadPoolMsgDownloaderService.class.getSimpleName();
    private ExecutorService mPool;

    public ThreadPoolMsgDownloaderService() {
        mPool = Executors.newFixedThreadPool(4);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                Messenger messenger = IntentExtraData.getMessenger(intent);
                Message msg = Message.obtain();

                // Loads image
                try {
                    Bitmap image = DownloaderUtils.loadImageSync(intent, new CancellationSignal());
                    MessageUtils.setBitmap(image, msg, null);
                }
                catch (Exception ex) {
                    MessageUtils.setError(msg, ex);
                }

                // Sends message
                try {
                    messenger.send(msg);
                }
                catch (RemoteException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }
        });
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
