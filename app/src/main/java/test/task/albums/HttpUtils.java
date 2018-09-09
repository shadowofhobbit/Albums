package test.task.albums;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import test.task.albums.api.ItunesService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class contains utility methods dealing with http
 */

public final class HttpUtils {

    private static final String URL = "https://itunes.apple.com/";

    private HttpUtils() {
    }

    private static ItunesService itunesService;

    public static ItunesService getItunesService() {
        if (itunesService == null) {
            Retrofit build = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URL).build();
           itunesService = build.create(ItunesService.class);
        }
        return itunesService;
    }

    public static boolean isConnectedToNetwork(Context appContext) {
        ConnectivityManager manager = (ConnectivityManager) appContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnected());
    }

}
