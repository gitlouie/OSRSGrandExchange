package com.example.android.oldschoolRS;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.android.oldschoolRS.utilities.NetworkUtils;


public class MainActivity extends AppCompatActivity implements
        DisplayAdapter.ListItemClickListener {

    //region Private Variables
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText mSearchBoxEditText;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private DisplayAdapter mAdapter;
    private RecyclerView mNumbersList;

    private Toast mToast;

    private List<JSONObject> mItemList = new ArrayList<JSONObject>();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the grandExchange repository, and fires off an AsyncTask
     * to perform the GET request.
     */
    private void makeGrandExchangeSearchQuery() {
        String grandExchangeQuery = mSearchBoxEditText.getText().toString();
        URL[] grandExchangeSearchUrl = NetworkUtils.buildUrl(grandExchangeQuery.toLowerCase());

        // Clear the items list that populates the RecycleView each time a new item
        // query is being made
        mItemList.clear();

        // Create Async tasks to query the item specified by the user. The OSRS API only returns
        // 12 items at a time. Currently only 5 tasks are being created for a max of 60 items.
        for (URL url : grandExchangeSearchUrl) {
            new GrandExchangeQueryTask().execute(url);
        }
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     */
    private void showJsonDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     */
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class GrandExchangeQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String grandExchangeSearchResults = null;
            try {
                grandExchangeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return grandExchangeSearchResults;
        }

        @Override
        protected void onPostExecute(String grandExchangeSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (grandExchangeSearchResults != null && !grandExchangeSearchResults.equals("")) {

                try {
                    final JSONObject obj = new JSONObject(grandExchangeSearchResults);
                    final JSONArray items = obj.getJSONArray("items");

                    // Empty the recycler view if no items were found
                    if (items.length() == 0) {
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    // Add all items into a list to later display on the RecyclerView
                    for (int i = 0; i < items.length(); i++) {
                        final JSONObject item = items.getJSONObject(i);
                        mItemList.add(item);

                    }
                    showJsonDataView();

                    mAdapter = new DisplayAdapter(mItemList.size(), mItemList,
                            MainActivity.this);
                    mNumbersList.setAdapter(mAdapter);
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }

            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        // If search button is clicked execute the query
        if (itemThatWasClickedId == R.id.action_search) {

            //Closes the keyboard
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            makeGrandExchangeSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex){
        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }
}
