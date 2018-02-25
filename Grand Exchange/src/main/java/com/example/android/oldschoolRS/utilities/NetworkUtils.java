package com.example.android.oldschoolRS.utilities;

import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    //region Private Variables
    final static String GRANDEXCHANGE_BASE_URL =
            "https://api.buying-gf.com/ge/search/";
    final static String OSRS_BASE_URL =
            "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";

    final static String PARAM_QUERY = "a";
    final static String ITEM_QUERY = "i";

    //endregion

    /**
     * Builds the URL used to query OSRS Grand Exchange.
     *
     * @param grandExchangeSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the OSRS Grand Exchange.
     */
    public static URL buildUrl(String grandExchangeSearchQuery, String source) {

        String baseURL = null;

        if(source == "OSRS_API"){
            baseURL = OSRS_BASE_URL;
        }else         {
            baseURL = GRANDEXCHANGE_BASE_URL;
        }
        String builtUri = baseURL + grandExchangeSearchQuery;

        URL url = null;
        try {
            url = new URL(builtUri);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }

    }
}