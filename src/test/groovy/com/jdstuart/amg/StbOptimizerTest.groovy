package com.jdstuart.amg;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;

import org.junit.Test;

class StbOptimizerTest
{

   @Test
   public void testIsViewForRange()
   {
      StbOptimizer optimizer = new StbOptimizer()
      
      String fivePM = '2013-08-22 17:00:00'
      String sevenPM = '2013-08-22 19:00:00'
      SimpleDateFormat format = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
      Date start = format.parse(fivePM)
      Date end = format.parse(sevenPM)
      
      Date ti1 = format.parse('2013-08-22 17:15:00')
      Date to1 = format.parse('2013-08-22 17:23:00')
      assertTrue optimizer.isViewForTimeRange(ti1, to1, start, end)
      
      Date ti2 = format.parse('2013-08-22 16:45:00')
      Date to2 = format.parse('2013-08-22 17:05:00')
      assertTrue optimizer.isViewForTimeRange(ti2, to2, start, end)
      
      Date ti3 = format.parse('2013-08-22 17:15:00')
      Date to3 = format.parse('2013-08-22 17:18:00')
      assertFalse optimizer.isViewForTimeRange(ti3, to3, start, end)
      
      Date ti4 = format.parse('2013-08-22 16:45:00')
      Date to4 = format.parse('2013-08-22 17:02:00')
      assertFalse optimizer.isViewForTimeRange(ti4, to4, start, end)
      
      Date ti5 = format.parse('2013-08-22 18:45:00')
      Date to5 = format.parse('2013-08-22 19:03:00')
      assertTrue optimizer.isViewForTimeRange(ti5, to5, start, end)
      
      Date ti6 = format.parse('2013-08-22 18:56:00')
      Date to6 = format.parse('2013-08-22 19:15:00')
      assertFalse optimizer.isViewForTimeRange(ti6, to6, start, end)
      
      Date ti7 = format.parse('2013-08-22 12:15:00')
      Date to7 = format.parse('2013-08-22 17:30:00')
      assertFalse optimizer.isViewForTimeRange(ti7, to7, start, end)
   }

}
