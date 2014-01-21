package com.jdstuart.amg

/**
 * Simple bean object for storing an STB device_id.
 * 
 * @author jdstuart
 * 
 */
class StbDevice implements Comparable<StbDevice>
{
   String id
   
   StbDevice(String id) 
   { 
      this.id = id
   }
   
   @Override
   public int compareTo(StbDevice o)
   {
      return this.id.compareTo(o.id);
   }
}