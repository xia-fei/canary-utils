package com.qccr.saas.wing.insight.test;

import com.qccr.commons.datetime.DateUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

public class DateTest {
    @Test
    public  void test1() throws ParseException {
        Calendar calendar=Calendar.getInstance();
        calendar.set(2017, Calendar.JANUARY,1);
        for (int i = 0; i < 100; i++) {
            int week=calendar.get(Calendar.WEEK_OF_YEAR);
            System.out.println(DateUtils.formateDate(calendar.getTime())+" "+week);
            calendar.add(Calendar.DATE,1);
        }
    }
}
