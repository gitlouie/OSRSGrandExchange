package com.example.android.oldschoolRS;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.oldschoolRS.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


/**
 * Created by Louie on 2/24/2018.
 */

public class itemSelected extends AppCompatActivity  {
    //region Private Variables
    private TextView mItemName;
    private TextView mItemDesc;
    private ImageView mItemIcon;
    private ImageView mMemberIcon;
    
    private TextView mItemPrice;
    private TextView mItemChangeToday;
    private TextView mItemChangeThirty;
    private TextView mItemChangeNinty;
    private TextView mItemChangeOneHundredEighty;


    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selected);

        Intent intent = getIntent();
        String itemID = intent.getStringExtra("key");

        mItemIcon = (ImageView) findViewById(R.id.item_image);
        mMemberIcon = (ImageView) findViewById(R.id.item_member);
        mItemName = (TextView) findViewById(R.id.item_name);
        mItemDesc = (TextView) findViewById(R.id.item_desc);

        mItemPrice = (TextView) findViewById(R.id.item_price);
        mItemChangeToday = (TextView) findViewById(R.id.item_today_change_price);
        mItemChangeThirty = (TextView) findViewById(R.id.item_one_month_price_variation);
        mItemChangeNinty = (TextView) findViewById(R.id.item_three_month_price_variation);
        mItemChangeOneHundredEighty = (TextView) findViewById(R.id.item_six_month_price_variation);
        
        URL grandExchangeSearchResults = NetworkUtils.buildUrl(itemID, "OSRS_API");
        new GEQueryTask().execute(grandExchangeSearchResults);

    }

    public class GEQueryTask extends AsyncTask<URL, Void, String> {


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String GEResults = null;
            try {
                GEResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return GEResults;
        }

        @Override
        protected void onPostExecute(String GEResults) {

            if (GEResults != null && !GEResults.equals("")) {

                try {
                    final JSONObject obj = new JSONObject(GEResults);
                    final JSONObject item = obj.getJSONObject("item");
                    final JSONObject currentTrend = item.getJSONObject("current");
                    final JSONObject todayChange = item.getJSONObject("today");
                    final JSONObject thirtyDayChange = item.getJSONObject("day30");
                    final JSONObject nintyDayChange = item.getJSONObject("day90");
                    final JSONObject oneHundredEightyDayChange = item.getJSONObject("day180");

                    Item iterData = new Item();

                    iterData.setID(item.getString("id"));
                    iterData.setDescription(item.getString("description"));
                    iterData.setName(item.getString("name"));
                    iterData.setMembers(TextUtils.equals(item.getString("members"),
                            "true"));

                    iterData.setPrice(currentTrend.getString("price"));
                    iterData.setCurrent(todayChange.getString("price"));
                    iterData.setThirtyDayTrend(thirtyDayChange.getString("change"));
                    iterData.setNintyDayTrend(nintyDayChange.getString("change"));
                    iterData.setOneHundredEightyDayTrend(oneHundredEightyDayChange.getString(
                            "change"));


                    // Load the image onto the ImageView
                    Picasso.with(itemSelected.this).load(iterData.getIcon())
                            .error(R.mipmap.ic_launcher)
                            .into(mItemIcon);

                    // If the item is a members item, display the members icon
                    if (iterData.getMembers()) {
                        mMemberIcon.setVisibility(View.VISIBLE);
                    } else {
                        mMemberIcon.setVisibility(View.GONE);
                    }

                    mItemName.setText(iterData.getName());
                    mItemDesc.setText(iterData.getDescription());
                    mItemPrice.setText(iterData.getPrice());
                    mItemChangeToday.setText(iterData.getCurrent());
                    mItemChangeThirty.setText(iterData.getThirtyDayTrend());
                    mItemChangeNinty.setText(iterData.getNintyDayTrend());
                    mItemChangeOneHundredEighty.setText(iterData.getOneHundredEightyDayTrend());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
