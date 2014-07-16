package choiceware.com.ultimateimagedownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Created by andyc on 7/14/2014.
 */
public class StartedServiceWithMessengerDownloader extends AbstractDownloader {

    private static final String TAG = StartedServiceWithMessengerDownloader.class.getSimpleName();
    private Class mServiceClass;

    public StartedServiceWithMessengerDownloader(int resId, Class serviceClass) {
        super(MyApplication.getStrFromContext(resId));
        mServiceClass = serviceClass;
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {

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
