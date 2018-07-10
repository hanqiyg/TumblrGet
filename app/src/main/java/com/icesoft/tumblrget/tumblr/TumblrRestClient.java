package com.icesoft.tumblrget.tumblr;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

/**
 * @author ice<hanqiyg@gmail.com>
 */
public class TumblrRestClient
{
    /**
     * https://api.tumblr.com/v2/blog/chicaoni/posts?api_key=MfA6BDjf9VUaGZhk0Qzc9mQxMoqrGAGbYNsLBM6i8ZZQDTQYaQ&id=173956820507
     */
    private static final String API_KEY = "MfA6BDjf9VUaGZhk0Qzc9mQxMoqrGAGbYNsLBM6i8ZZQDTQYaQ";
    private static final String BASE_URL = "https://api.tumblr.com/v2/blog/%s/posts?api_key=" + API_KEY + "&id=%d";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String blogName,Long postId,AsyncHttpResponseHandler responseHandler) {
        String url = getURL(blogName,postId);
        RequestHandle requestHandler = client.get(url, null, responseHandler);
        requestHandler.setTag(postId);
    }
    public static void cancel(Object tag)
    {
        client.cancelRequestsByTAG(tag,true);
    }
    public static String getURL(String blogName,Long postId)
    {
        return String.format(BASE_URL, blogName, postId);
    }
}
