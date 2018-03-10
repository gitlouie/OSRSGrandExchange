package com.example.android.oldschoolRS;

/**
 * Created by Louie on 3/9/2018.
 */

public class Period implements java.io.Serializable{
    private String m_datePattern;
    private int m_numOfLabels;
    private int m_numOfDays;
    
    public Period(String datePattern, int numLabels, int numDays){
        m_datePattern = datePattern;
        m_numOfLabels = numLabels;
        m_numOfDays = numDays;
    }

    public String getDatePattern() {
        return m_datePattern;
    }

    public int getM_numOfLabels() {
        return m_numOfLabels;
    }

    public int getM_numOfDays() {
        return m_numOfDays;
    }

}
