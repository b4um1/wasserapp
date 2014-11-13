/*
 * 
 */
package at.fhhgb.mc.wasserapp.rssfeed;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class RssService.
 */
public class RssService extends IntentService {

    /** The Constant TAG. */
    private static final String TAG = RssService.class.getSimpleName();

    /** The Constant PENDING_RESULT_EXTRA. */
    public static final String PENDING_RESULT_EXTRA = "pending_result";
    
    /** The Constant URL_EXTRA. */
    public static final String URL_EXTRA = "url";
    
    /** The Constant RSS_RESULT_EXTRA. */
    public static final String RSS_RESULT_EXTRA = "url";

    /** The Constant RESULT_CODE. */
    public static final int RESULT_CODE = 0;
    
    /** The Constant INVALID_URL_CODE. */
    public static final int INVALID_URL_CODE = 1;
    
    /** The Constant ERROR_CODE. */
    public static final int ERROR_CODE = 2;

    /** The parser. */
    private FeedParser parser;

    /**
     * Instantiates a new rss service.
     */
    public RssService() {
        super(TAG);

        // make one and re-use, in the case where more than one intent is queued
        parser = new FeedParser();
    }

    /* (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        InputStream in = null;
        try {
            try {
                URL url = new URL(intent.getStringExtra(URL_EXTRA));
                FeedRss rss = parser.parse(in = url.openStream());

                Intent result = new Intent();
                result.putExtra(RSS_RESULT_EXTRA, rss);
                reply.send(this, RESULT_CODE, result);
                
            } catch (MalformedURLException exc) {
                reply.send(INVALID_URL_CODE);
            } catch (Exception exc) {
                // could do better by treating the different sax/xml exceptions individually
                reply.send(ERROR_CODE);
            }
        } catch (PendingIntent.CanceledException exc) {
            Log.i(TAG, "reply cancelled", exc);
        }
    }
}