package com.jdstuart.amg

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
