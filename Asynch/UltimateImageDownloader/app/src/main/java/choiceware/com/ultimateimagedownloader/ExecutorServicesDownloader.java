package choiceware.com.ultimateimagedownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by andyc on 7/5/2014.
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
                DownloaderUtils.loadImageWithCallbackSync(mCallback, loadOpt);
                notifyIsDoneLoading();
            }
        });
    }
}
