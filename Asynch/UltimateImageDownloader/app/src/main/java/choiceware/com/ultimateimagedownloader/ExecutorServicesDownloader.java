package choiceware.com.ultimateimagedownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by achoi on 7/5/2014.
 */
public class ExecutorServicesDownloader extends AbstractDownloader {

    private ExecutorService mPool;

    public ExecutorServicesDownloader() {
        super(MyApplication.getStrFromContext(R.string.ExecutorServicesThread));
        mPool = Executors.newFixedThreadPool(1);
    }

    @Override
    protected void onLoad(final LoadOptions loadOpt) {
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DownloaderUtils.loadImageWithCallbackSync(mCallback, loadOpt);
                }
                finally {
                    notifyIsDoneLoading();
                }
            }
        });
    }
}
