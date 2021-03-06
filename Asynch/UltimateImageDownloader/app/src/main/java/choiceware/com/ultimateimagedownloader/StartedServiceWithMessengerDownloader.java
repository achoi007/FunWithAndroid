package choiceware.com.ultimateimagedownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Class to use a "started service" as image downloader.
 *
 * Created by achoi on 7/14/2014.
 */
public class StartedServiceWithMessengerDownloader extends AbstractDownloader {

    private static final String TAG = StartedServiceWithMessengerDownloader.class.getSimpleName();
    private Class mServiceClass;

    public StartedServiceWithMessengerDownloader(int strResId, Class serviceClass) {
        super(MyApplication.getStrFromContext(strResId));
        mServiceClass = serviceClass;
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {

        loadOpt.getCxlSig().setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                Log.d(TAG, "Stopping service");
                Intent intent = new Intent(MyApplication.getContext(), mServiceClass);
                MyApplication.getContext().stopService(intent);
            }
        });

        // Create service intent
        Intent intent = new Intent(MyApplication.getContext(), mServiceClass);
        intent.setData(loadOpt.getUri());
        IntentExtraData.setMaxHeight(intent, loadOpt.getMaxHeight());
        IntentExtraData.setMaxWidth(intent, loadOpt.getMaxWidth());

        // Set messenger to communicate with service
        Handler serviceHdl = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                StartedServiceWithMessengerDownloader.this.processMessage(msg);
            }
        };
        Messenger messenger = new Messenger(serviceHdl);
        IntentExtraData.setMessenger(intent, messenger);

        // Start service
        Log.d(TAG, "Starting service with intent " + intent);
        MyApplication.getContext().startService(intent);
    }

    private void processMessage(Message message) {
        try {
            if (MessageUtils.getError(message) != null) {
                Log.d(TAG, "procMsg: error");
                getCallback().onError(MessageUtils.getError(message));
            }
            else if (MessageUtils.isCancelled(message)) {
                Log.d(TAG, "procMsg: cancelled");
                getCallback().onCancelled();
            }
            else {
                Log.d(TAG, "procMsg: image");
                Bitmap image = MessageUtils.getBitmap(message, null, true);
                getCallback().onImage(image);
            }
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            getCallback().onError(ex);
        }
        finally {
            notifyIsDoneLoading();
        }
    }
}
