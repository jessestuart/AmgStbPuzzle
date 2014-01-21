package com.jdstuart.amg

/**
 * Wrapper object for mapping a STB viewing date to its corresponding
 * network.
 * 
 * Implements {@link Comparable} to allow for sorting of a list of data points
 * by date, so they can be iterated through chronologically. 
 * 
 * @author jdstuart
 *
 */
class StbDataPoint implements Comparable<StbDataPoint>
{
   StbNetwork network
   
   Date date
   
   StbDataPoint(StbNetwork network, Date date)
   {
      this.network = network
      this.date = date
   }

   @Override
   public int compareTo(StbDataPoint o)
   {
      return this.date.compareTo(o.date)
   }
   
   public String toString()
   {
      return "Network : $network Date : $date"
   }
   
}
