package com.example.android.oldschoolRS;

import android.content.Context;

import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.DateFormat;

/**
 * Created by Louie on 2/26/2018.
 */

public class AxisFormater extends DateAsXAxisLabelFormatter {

    public AxisFormater(Context context, DateFormat dateFormat) {
        super(context, dateFormat);
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if(isValueX) {
            return super.formatLabel(value, isValueX);
        }
        else {
            if(value > 1000000) {
                return String.format("%.1fM  ", value/1000000);
            }
            else if(value > 1000) {
                return String.format("%.1fK  ", value/1000);
            }

            return ((int) value) + "gp  ";
        }
    }
}
