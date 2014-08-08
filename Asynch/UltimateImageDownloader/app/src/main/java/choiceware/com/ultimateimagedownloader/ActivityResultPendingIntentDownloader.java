package choiceware.com.ultimateimagedownloader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by achoi on 7/16/2014.
 */
public class ActivityResultPendingIntentDownloader extends AbstractDownloader {


    public static final int REQ_FACTORIAL = 1;

    private static final String TAG = ActivityResultPendingIntentDownloader.class.getSimpleName();
    private Class mServiceClass;
    private Activity mAct;

    public ActivityResultPendingIntentDownloader(int strResId, Class serviceClass, Activity act) {
        super(MyApplication.getStrFromContext(strResId));
        mServiceClass = serviceClass;
        mAct = act;
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {

        // Create pending intent to call activity onResult
        PendingIntent pi = mAct.createPendingResult(REQ_FACTORIAL, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create service intent
        Intent servIntent = new Intent(mAct, mServiceClass);
        servIntent.putExtra(IntentExtraData.PENDING_INTENT, pi);
        servIntent.setData(loadOpt.getUri());
        IntentExtraData.setMaxHeight(servIntent, loadOpt.getMaxHeight());
        IntentExtraData.setMaxWidth(servIntent, loadOpt.getMaxWidth());

        // Start service
        mAct.startService(servIntent);
        notifyIsDoneLoading();
    }
}
