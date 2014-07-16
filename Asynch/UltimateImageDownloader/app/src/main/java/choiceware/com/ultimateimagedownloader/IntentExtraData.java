package choiceware.com.ultimateimagedownloader;

import android.content.Intent;
import android.net.Uri;
import android.os.Messenger;
import android.os.Parcelable;

/**
 * Created by alang_000 on 7/13/2014.
 */
public class IntentExtraData {

    public static final String MAX_WIDTH = "MAX_WIDTH";
    public static final String MAX_HEIGHT = "MAX_HEIGHT";
    public static final String MESSENGER = "MESSENGER";
    public static final String IMAGE = "IMAGE";
    public static final String PENDING_INTENT = "PENDING_INTENT";
    public static final String EXCEPTION = "EXCEPTION";

    public static int getMaxWidth(Intent i) {
        return i.getIntExtra(MAX_WIDTH, 100);
    }

    public static void setMaxWidth(Intent i, int maxWidth) {
        i.putExtra(MAX_WIDTH, maxWidth);
    }

    public static int getMaxHeight(Intent i) {
        return i.getIntExtra(MAX_HEIGHT, 100);
    }

    public static void setMaxHeight(Intent i, int maxHeight) {
        i.putExtra(MAX_HEIGHT, maxHeight);
    }

    public static Messenger getMessenger(Intent i) {

        Parcelable p = i.getParcelableExtra(MESSENGER);

        if (p == null) {
            throw new IllegalArgumentException("intent has no MESSENGER parceable extra");
        }

        if (!(p instanceof Messenger)) {
            throw new IllegalArgumentException("MESSENGER parceable is not a Messenger");
        }

        return (Messenger) p;
    }

    public static void setMessenger(Intent i, Messenger messenger) {
        i.putExtra(MESSENGER, messenger);
    }
}
