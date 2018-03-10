package com.example.android.oldschoolRS;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class GraphGrandExchangeItem extends Fragment {

    private GraphView graph;
    private AlertDialog m_datapointDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_frag, container, false);

        graph = (GraphView) view.findViewById(R.id.grandExchange_graph);

        ArrayList<DataPoint> dailyList =
                (ArrayList<DataPoint>)getArguments().getSerializable("datapoints");
        ArrayList<DataPoint> avgList =
                (ArrayList<DataPoint>)getArguments().getSerializable("averages");

        Period period = (Period) getArguments().getSerializable("Period");

        DataPoint[] dailyDataPointArray = dailyList.toArray(new DataPoint[dailyList.size()]);
        DataPoint[] trendDataPointArray = avgList.toArray(new DataPoint[avgList.size()]);

        DataPoint[] dailyPeriodArray = Arrays.copyOfRange(dailyDataPointArray, Math.max(0,
                dailyDataPointArray.length - period.getM_numOfDays()), dailyDataPointArray.length);
        DataPoint[] trendPeriodArray = Arrays.copyOfRange(trendDataPointArray, Math.max(0,
                trendDataPointArray.length - period.getM_numOfDays()), trendDataPointArray.length);

        LineGraphSeries<DataPoint> dailySeries = new LineGraphSeries<>(dailyPeriodArray);
        LineGraphSeries<DataPoint> trendSeries = new LineGraphSeries<>(trendPeriodArray);

        dailySeries.setDrawDataPoints(true);
        dailySeries.setTitle("Daily Average");
        dailySeries.setColor(Color.RED);
        graph.addSeries(dailySeries);

        trendSeries.setDrawDataPoints(true);
        trendSeries.setTitle("Trend");
        trendSeries.setDataPointsRadius(6f);
        graph.addSeries(trendSeries);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setLabelFormatter(new AxisFormater(getActivity(),
                new SimpleDateFormat(period.getDatePattern())));
        graph.getGridLabelRenderer().setNumHorizontalLabels(period.getM_numOfLabels());
        graph.getViewport().setMinX(dailySeries.getLowestValueX());
        graph.getViewport().setMaxX(dailySeries.getHighestValueX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        dailySeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                CreateAlertDialog("Daily Average", dataPoint.getX(), dataPoint.getY());

            }
        });

        trendSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                CreateAlertDialog("Trend", dataPoint.getX(), dataPoint.getY());

            }
        });

        return  view;
    }


    private void CreateAlertDialog(String title, double date, double price){

        //Do not display more than one dialog at a time
        if( m_datapointDialog != null && m_datapointDialog.isShowing() ) return;

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        String dateDisplay = sdf.format(new Date((long)date));

        NumberFormat nf = NumberFormat.getInstance();
        String itemPrice = nf.format(price) + "gp";

        //Build and show the dialog to the user
        String displayText = "Date\n" + dateDisplay + "\n\nPrice\n" + itemPrice;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(displayText);

        m_datapointDialog = builder.create();
        m_datapointDialog.show();

    }

}
