package com.example.android.oldschoolRS.utilities;

import android.net.Uri;

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
            "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/items.json?category=1";

    final static String PARAM_QUERY = "alpha";
    final static String PAGE_QUERY = "page";

    //endregion

    /**
     * Builds the URL used to query OSRS Grand Exchange.
     *
     * @param grandExchangeSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the OSRS Grand Exchange.
     */
    public static URL[] buildUrl(String grandExchangeSearchQuery) {
        URL[] urls = new URL[5];

        // Currently creating 5 URLs to be passed back.. Will revisit this later.
        for(int i = 0; i <urls.length; i++) {
            Uri builtUri = Uri.parse(GRANDEXCHANGE_BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, grandExchangeSearchQuery)
                    .appendQueryParameter(PAGE_QUERY, Integer.toString(i+1))
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
                urls[i] = url;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
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